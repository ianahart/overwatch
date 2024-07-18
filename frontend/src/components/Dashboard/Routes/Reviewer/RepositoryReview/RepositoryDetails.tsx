import { useSelector } from 'react-redux';
import { nanoid } from 'nanoid';
import { IoLink } from 'react-icons/io5';

import { TRootState } from '../../../../../state/store';
import { IProgressMapper } from '../../../../../interfaces';

const RepositoryDetails = () => {
  const { repository, repositoryLanguages } = useSelector((store: TRootState) => store.repositoryTree);
  console.log(repository);

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

  return (
    <div className="bg-gray-950 w-full p-4 text-gray-400">
      {/*Header*/}
      <div className="flex items-center">
        <img className="h-16 w-16 rounded-lg" src={repository.avatarUrl} alt={repository.repoName} />
        <h2 className="text-gray-400 ml-4 text-xl">{repository.repoName}</h2>
        <a className="block m-2 text-xl" href={repository.repoUrl}>
          <IoLink />
        </a>
      </div>
      {/*End Header*/}
      {/*Stats*/}
      <div className="my-8">
        <div
          className={`my-2 text-gray-800 text-center rounded-lg shadow-md w-28 font-bold ${progressMapper[progressStatus]?.background}`}
        >
          {progressMapper[progressStatus]?.text}
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
      {/*End Stats*/}
      {/*Comment*/}
      <div className="my-8">
        <p>The user's instruction(s) and comment(s)</p>
        <div className="my-4 max-w-[550px] bg-gray-900 p-4 rounded">
          <p>{repository.comment}</p>
        </div>
      </div>
      {/*End Comment*/}
      {/*Feedback*/}
      {/*End Feedback*/}
    </div>
  );
};

export default RepositoryDetails;
