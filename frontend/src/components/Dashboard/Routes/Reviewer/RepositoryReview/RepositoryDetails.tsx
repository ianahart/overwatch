import { useSelector } from 'react-redux';
import { useEffect } from 'react';
import { nanoid } from 'nanoid';
import { IoLink } from 'react-icons/io5';

import { TRootState, useUpdateRepositoryReviewStartTimeMutation } from '../../../../../state/store';
import { IProgressMapper } from '../../../../../interfaces';
import ReviewEditor from './ReviewEditor';

const RepositoryDetails = () => {
  const { token } = useSelector((store: TRootState) => store.user);
  const { repository, repositoryLanguages } = useSelector((store: TRootState) => store.repositoryTree);
  const [updateRepositoryReviewStartTime] = useUpdateRepositoryReviewStartTimeMutation();
  const progressMapper: IProgressMapper = {
    INCOMPLETE: { text: 'Incomplete', background: 'bg-gray-400' },
    INPROGRESS: { text: 'In progress', background: 'bg-blue-400' },
    COMPLETED: { text: 'Completed', background: 'bg-green-400' },
  };

  const progressStatus = repository.status as keyof IProgressMapper;

  const languageIcon = (repositoryLanguage: string) => {
    if (repositoryLanguage.toLowerCase().includes('css')) {
      return 'css3';
    }
    if (repositoryLanguage.toLowerCase().includes('html')) {
      return 'html5';
    }

    return repositoryLanguage.toLowerCase();
  };

  useEffect(() => {
    if (repository.reviewStartTime === null && repository.status === 'INCOMPLETE') {
      const payload = {
        reviewStartTime: repository.reviewStartTime,
        repositoryId: repository.id,
        status: repository.status,
        token,
      };
      updateRepositoryReviewStartTime(payload)
        .unwrap()
        .then(() => {})
        .catch((err) => {
          console.log(err);
        });
    }
  }, [repository]);

  return (
    <div className="bg-gray-950 w-full p-4 text-gray-400">
      <div className="flex items-center">
        <img className="h-16 w-16 rounded-lg" src={repository.avatarUrl} alt={repository.repoName} />
        <h2 className="text-gray-400 ml-4 text-xl">{repository.repoName}</h2>
        <a className="block m-2 text-xl" href={repository.repoUrl}>
          <IoLink />
        </a>
      </div>
      <div className="my-8">
        <div className="flex items-center">
          <div
            className={`my-2 text-gray-800 text-center rounded-lg shadow-md w-28 font-bold ${progressMapper[progressStatus]?.background}`}
          >
            {progressMapper[progressStatus]?.text}
          </div>
          <p className="ml-2">
            Time spent reviewing: <span className="text-green-400 text-sm font-obold">{repository.reviewDuration}</span>
          </p>
        </div>
        <p>
          Main language is{' '}
          <span>
            <i className={`mr-1 text-green-400 devicon-${languageIcon(repository.language)}-plain`}></i>
          </span>
          <span className="font-bold">{repository.language}</span>
        </p>
        <div className="my-4">
          <p>Other languages include:</p>
          <ul className="flex flex-wrap w-[180px]">
            {repositoryLanguages.map((repositoryLanguage) => {
              return repositoryLanguage !== repository.language ? (
                <li key={nanoid()} className="flex items-center m-2">
                  <i className={`mr-1 text-green-400 devicon-${languageIcon(repositoryLanguage)}-plain`}></i>
                  <p>{repositoryLanguage}</p>
                </li>
              ) : null;
            })}
          </ul>
        </div>
      </div>
      <div className="my-8">
        <p>The user's instruction(s) and comment(s)</p>
        <div className="my-4 max-w-[550px] bg-gray-900 p-4 rounded">
          <p>{repository.comment.length ? repository.comment : 'The user did not leave any comments for you.'}</p>
        </div>
      </div>
      <div className="my-8">
        <p>
          Your feedback for the repository <span className="font-bold text-green-400">{repository.repoName}</span>
        </p>
        <div className="my-4">
          <ReviewEditor />
        </div>
      </div>
    </div>
  );
};

export default RepositoryDetails;
