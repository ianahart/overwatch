import { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import { Session } from '../../../../../util/SessionService';
import {
  TRootState,
  useCreateUserRepositoryMutation,
  useFetchGitHubUserReposQuery,
  useLazyFetchGitHubUserReposQuery,
  useLazyFetchProfilePackagesQuery,
} from '../../../../../state/store';
import { retrieveTokens } from '../../../../../util';
import { IGitHubRepositoryPreview } from '../../../../../interfaces';
import Repository from './Repository';
import { nanoid } from 'nanoid';

export interface IPackagePlan {
  price: string;
  plan: string;
}

const RepositoryList = () => {
  const navigate = useNavigate();
  const githubId = Session.getItem('github_access_token') ?? '';
  const token = retrieveTokens().token;
  const [page, setPage] = useState(1);
  const [comment, setComment] = useState('');
  const [reviewType, setReviewType] = useState('');
  const [repositories, setRepositories] = useState<IGitHubRepositoryPreview[]>([]);
  const [nextPageUrl, setNextPageUrl] = useState('');
  const [error, setError] = useState('');
  const { data } = useFetchGitHubUserReposQuery(
    { token, githubId: Number.parseInt(githubId), page },
    { skip: !token || !githubId }
  );
  const [packages, setPackages] = useState<IPackagePlan[]>([]);
  const [selectedPackagePrice, setSelectedPackagePrice] = useState('');
  const [paginateRepositories] = useLazyFetchGitHubUserReposQuery();
  const [getProfilePackages] = useLazyFetchProfilePackagesQuery();
  const [createUserRepository] = useCreateUserRepositoryMutation();
  const { selectedReviewer } = useSelector((store: TRootState) => store.addReview);
  const [selectedRepository, setSelectedRepository] = useState<IGitHubRepositoryPreview | null>(null);
  const { user } = useSelector((store: TRootState) => store.user);

  useEffect(() => {
    getProfilePackages({ userId: selectedReviewer.receiverId, token })
      .unwrap()
      .then((res) => {
        const plans = ['basic', 'standard', 'pro'];

        setPackages(
          [...Object.values(res.data)].map((pckge, index) => {
            return { price: pckge.price, plan: plans[index] };
          })
        );
      })
      .catch((err) => {
        console.log(err);
      });
  }, [selectedReviewer.id, token]);

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

      const response = await paginateRepositories({ token, page, githubId: Number.parseInt(githubId) }).unwrap();

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

  const addReview = (): void => {
    setError('');
    if (selectedRepository === null) {
      return;
    }
    const payload = {
      reviewerId: selectedReviewer.receiverId,
      ownerId: user.id,
      repoName: selectedRepository.fullName,
      repoUrl: selectedRepository.htmlUrl,
      avatarUrl: selectedRepository.avatarUrl,
      comment,
      language: selectedRepository.language,
      reviewType,
      paymentPrice: Number.parseFloat(selectedPackagePrice),
    };

    if (!selectedPackagePrice.length) {
      setError('Please select a package plan to continue');
      return;
    }

    if (!reviewType.length) {
      setError('Please select the type of review you are requesting for');
      return;
    }

    createUserRepository({ payload, token })
      .unwrap()
      .then(() => {
        navigate(`/dashboard/${user.slug}/user/reviews`);
      })
      .catch((err) => {
        applyServerErrors(err.data);
      });
  };

  const selectRepository = (repository: IGitHubRepositoryPreview | null): void => {
    setSelectedRepository(repository);
  };

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
            data-testid="select-review-type"
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
      <div className="my-8">
        <p className="text-sm">What plan do you want?</p>
        <p className="text-xs mb-2">(Refer to their profile for details)</p>
        <select
          data-testid="select-package-plan"
          onChange={(e) => setSelectedPackagePrice(e.target.value)}
          value={selectedPackagePrice}
          className="w-full bg-transparent border border-gray-800 p-1"
        >
          <option value="" disabled>
            Select package plan
          </option>
          {packages.map((pckge) => {
            return (
              <option key={nanoid()} value={pckge.price}>
                {pckge.plan}-${pckge.price}
              </option>
            );
          })}
        </select>
      </div>
      <div className="my-1">
        <p className="text-gray-400">Click on a repository to request a review.</p>
      </div>
      <div className="overflow-scroll overflow-y-auto overflow-x-hidden h-[300px]">
        {repositories.map((repository) => {
          return <Repository key={repository.id} data={repository} selectRepository={selectRepository} />;
        })}
        {nextPageUrl.length > 0 && (
          <div className="flex justify-center">
            <button onClick={handleOnClickMore}>More repositories...</button>
          </div>
        )}
      </div>
      {selectedPackagePrice.length > 0 && selectedRepository !== null && (
        <div className="my-8">
          <p className="text-sm mb-2">
            You have selected <span className="font-bold text-green-400">{selectedRepository?.fullName}</span> as the
            repository you want reviewed
          </p>
          <p className="text-sm">
            You will be charged <span className="font-bold text-green-400">${selectedPackagePrice}</span> once the
            review is completed
          </p>
        </div>
      )}
      <div className="my-8">
        <button onClick={addReview} className="btn w-full">
          Submit
        </button>
      </div>
    </>
  );
};

export default RepositoryList;
