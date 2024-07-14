import { AiFillStar } from 'react-icons/ai';
import { IoLink } from 'react-icons/io5';

import { IGitHubRepositoryPreview } from '../../../../../interfaces';

export interface IRepositoryProps {
  data: IGitHubRepositoryPreview;
  addReview: (repoName: string, repoUrl: string, avatarUrl: string) => void;
}

const Repository = ({ data, addReview }: IRepositoryProps) => {
  const handleOnClick = () => {
    const { fullName: repoName, avatarUrl, htmlUrl: repoUrl } = data;
    addReview(repoName, avatarUrl, repoUrl);
  };

  return (
    <div className="my-2 p-2 cursor-pointer" onClick={handleOnClick}>
      <div className="flex items-center">
        <img className="w-6 h-6 rounded-lg mr-2" src={data.avatarUrl} alt={data.fullName} />
        <div>
          <p className="text-sm text-gray-400">{data.fullName}</p>
          <div className="flex justify-between">
            <div className="flex items-center">
              <AiFillStar className="text-yellow-400" />
              <p className="text-sm">{data.stargazersCount}</p>
            </div>
            <p className="text-xs">{data.language}</p>
            <a target="_blank" rel="noopener noreferrer" href={data.htmlUrl}>
              <IoLink className="cursor-pointer" />
            </a>
          </div>
        </div>
      </div>
      <div></div>
    </div>
  );
};

export default Repository;
