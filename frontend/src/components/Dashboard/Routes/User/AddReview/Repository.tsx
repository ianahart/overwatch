import { AiFillStar } from 'react-icons/ai';
import { IoLink } from 'react-icons/io5';

import { IGitHubRepositoryPreview } from '../../../../../interfaces';

export interface IRepositoryProps {
  data: IGitHubRepositoryPreview;
  selectRepository: (repository: IGitHubRepositoryPreview | null) => void;
}

const Repository = ({ data, selectRepository }: IRepositoryProps) => {
  const handleOnClick = () => {
    selectRepository(data);
  };

  return (
    <div data-testid="Repository" className="my-2 p-2 cursor-pointer" onClick={handleOnClick}>
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
