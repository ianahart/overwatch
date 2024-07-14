import { AiFillStar } from 'react-icons/ai';
import { IGitHubRepositoryPreview } from '../../../../../interfaces';

export interface IRepositoryProps {
  data: IGitHubRepositoryPreview;
}

const Repository = ({ data }: IRepositoryProps) => {
  return (
    <div className="my-2 p-2">
      <div className="flex items-center">
        <img className="w-6 h-6 rounded-lg mr-2" src={data.avatarUrl} alt={data.fullName} />
        <div>
          <a target="_blank" rel="noopener noreferrer" href={data.htmlUrl}>
            <p className="text-sm text-gray-400">{data.fullName}</p>
          </a>
          <div className="flex justify-between">
            <div className="flex items-center">
              <AiFillStar className="text-yellow-400" />
              <p className="text-sm">{data.stargazersCount}</p>
            </div>
            <p className="text-xs">{data.language}</p>
          </div>
        </div>
      </div>
      <div></div>
    </div>
  );
};

export default Repository;
