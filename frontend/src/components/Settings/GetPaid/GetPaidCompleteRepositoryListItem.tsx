import { FaPen } from 'react-icons/fa';
import { IRepositoryReview } from '../../../interfaces';
import { initializeName } from '../../../util';
import DashboardAvatar from '../../Dashboard/DashboardAvatar';
import dayjs from 'dayjs';

export interface IGetPaidCompleteRepositoryListItemProps {
  repository: IRepositoryReview;
}

const GetPaidCompleteRepositoryListItem = ({ repository }: IGetPaidCompleteRepositoryListItemProps) => {
  return (
    <li className="my-4 border p-1 rounded border-gray-800">
      <div className="flex items-end justify-between">
        <div className="items-end flex">
          <div>
            <p className={`md:flex hidden flex-col w-20 my-1 text-black p-1 text-xs rounded bg-green-400`}>Completed</p>
            <p className="text-xs">Completed on:{dayjs(repository.reviewEndTime).format('MM/D/YYYY')}</p>
            <div className="flex items-center">
              <DashboardAvatar
                abbreviation={initializeName(repository.firstName, repository.lastName)}
                url={repository.profileUrl}
                height="h-9"
                width="w-9"
              />
              <div className="ml-2">
                <p className="text-gray-400 text-sm">
                  {repository.firstName} {repository.lastName}
                </p>
              </div>
            </div>
          </div>
          <div className="text-sm flex items-center m-2 text-gray-400">
            <div className="flex items-center mx-2">
              <img className="rounded-full w-6 h-6 mr-1" src={repository.avatarUrl} alt={repository.repoName} />
              <a href={repository.repoUrl} rel="noreferrer noopener" target="_blank">
                <p>{repository.repoName}</p>
              </a>
            </div>
            <div className="md:flex hidden items-center mx-2">
              <FaPen className="text-sm text-gray-800 mr-1" />
              <p className="">{repository.language}</p>
            </div>
          </div>
        </div>
        <div className="flex flex-col">
          <p className="text-green-400">+ ${repository.paymentPrice}</p>
          <button className="btn !bg-blue-400">Get paid</button>
        </div>
      </div>
    </li>
  );
};

export default GetPaidCompleteRepositoryListItem;
