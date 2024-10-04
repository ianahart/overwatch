import { useCallback, useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import { Session } from '../../../../../util/SessionService';
import {
  TRootState,
  useCreateUserRepositoryMutation,
  useFetchGitHubUserReposQuery,
  useLazyFetchGitHubUserReposQuery,
} from '../../../../../state/store';
import { retrieveTokens } from '../../../../../util';
import { IGitHubRepositoryPreview } from '../../../../../interfaces';
import Repository from './Repository';

const RepositoryList = () => {
  const navigate = useNavigate();
  const accessToken = Session.getItem('github_access_token') ?? '';
  const token = retrieveTokens().token;
  const [page, setPage] = useState(1);
  const [comment, setComment] = useState('');
  const [reviewType, setReviewType] = useState('');
  const [repositories, setRepositories] = useState<IGitHubRepositoryPreview[]>([]);
  const [nextPageUrl, setNextPageUrl] = useState('');
  const [error, setError] = useState('');
  const { data } = useFetchGitHubUserReposQuery({ token, accessToken, page });
  const [paginateRepositories] = useLazyFetchGitHubUserReposQuery();
  const [createUserRepository] = useCreateUserRepositoryMutation();
  const { selectedReviewer } = useSelector((store: TRootState) => store.addReview);
  const { user } = useSelector((store: TRootState) => store.user);

  const reviewTypes = [
    { id: 1, name: 'Bug-fixes', value: 'BUG' },
    { id: 2, name: 'Feature-additions', value: 'FEATURE' },
    { id: 3, name: 'Optimizations', value: 'OPTIMIZATION' },
  ];

  useEffect(() => {
    if (data !== undefined) {
      setRepositories(data.data.repositories);
      setNextPageUrl(data.data.nextPageUrl);
    }
  }, [data]);

  const handleOnClickMore = async () => {
    try {
      if (nextPageUrl === '') return;
      setPage((prevState) => prevState + 1);

      const response = await paginateRepositories({ token, page, accessToken }).unwrap();

      setRepositories((prevState) => [...prevState, ...response.data.repositories]);
    } catch (err) {
      console.log(err);
    }
  };

  const applyServerErrors = <T extends object>(data: T) => {
    for (const [_, val] of Object.entries(data)) {
      setError(val);
      return;
    }
  };

  const addReview = useCallback(
    (repoName: string, repoUrl: string, avatarUrl: string, language: string) => {
      setError('');
      const payload = {
        reviewerId: selectedReviewer.receiverId,
        ownerId: user.id,
        repoName,
        repoUrl,
        avatarUrl,
        comment,
        language,
        reviewType,
      };

      if (!reviewType.length) {
        setError('Please select the type of review you are requesting for');
        return;
      }

      createUserRepository({ payload, token })
        .unwrap()
        .then((res) => {
          console.log(res);
          navigate(`/dashboard/${user.slug}/user/reviews`);
        })
        .catch((err) => {
          applyServerErrors(err.data);
        });
    },
    [selectedReviewer.receiverId, reviewType, navigate, comment, createUserRepository, applyServerErrors]
  );

  return (
    <>
      {error.length > 0 && (
        <div className="my-2">
          <p className="text-xs text-red-400">{error}</p>
        </div>
      )}
      <div className="my-8 w-full">
        <p className="text-sm">What type of review is this for?</p>
        <div className="my-1">
          <select
            value={reviewType}
            onChange={(e) => setReviewType(e.target.value)}
            className="w-full bg-transparent border border-gray-800 p-1"
          >
            <option value="" disabled>
              Select review type
            </option>
            {reviewTypes.map((rt) => {
              return (
                <option key={rt.id} value={rt.value}>
                  {rt.name}
                </option>
              );
            })}
          </select>
        </div>
      </div>
      <div className="my-8 w-full">
        <label className="text-sm" htmlFor="comment">
          Add comment(s) for the reviewer
        </label>
        <textarea
          value={comment}
          onChange={(e) => setComment(e.target.value)}
          id="comment"
          name="comment"
          className="text-sm p-1 w-full bg-transparent border-gray-800 border rounded min-h-20 resize-none"
        ></textarea>
      </div>

      <div className="my-1">
        <p className="text-gray-400">Click on a repository to request a review.</p>
      </div>
      <div className="overflow-scroll overflow-y-auto overflow-x-hidden h-[300px]">
        {repositories.map((repository) => {
          return <Repository key={repository.id} data={repository} addReview={addReview} />;
        })}
        {nextPageUrl.length > 0 && (
          <div className="flex justify-center">
            <button onClick={handleOnClickMore}>More repositories...</button>
          </div>
        )}
      </div>
    </>
  );
};

export default RepositoryList;
