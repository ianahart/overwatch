import { IRepositoryReview } from '../../../../../interfaces';
import dayjs from 'dayjs';
import DashboardAvatar from '../../../DashboardAvatar';
import { useMemo } from 'react';
import { FaPen } from 'react-icons/fa';
import { useSelector } from 'react-redux';
import { TRootState } from '../../../../../state/store';
import { Role } from '../../../../../enums';

export interface IRepositoryReviewListItemProps {
  data: IRepositoryReview;
}

const RepositoryReviewListItem = ({ data }: IRepositoryReviewListItemProps) => {
  const { user } = useSelector((store: TRootState) => store.user);
  const abbreviation = useMemo(() => {
    return data.firstName[0] + '.' + data.lastName[0];
  }, [data.firstName, data.lastName]);

  return (
    <li className="my-4 border p-1 rounded border-gray-800">
      <div className="flex items-center justify-between">
        <div className="items-center flex">
          <div className="flex items-center">
            <DashboardAvatar abbreviation={abbreviation} url={data.profileUrl} height="h-9" width="w-9" />
            <div className="ml-2">
              <p className="text-gray-400 text-sm">
                {data.firstName} {data.lastName}
              </p>
            </div>
          </div>
          <div className="text-sm flex items-center m-2 text-gray-400">
            <div className="flex items-center mx-2">
              <img className="rounded-full w-6 h-6 mr-1" src={data.avatarUrl} alt={data.repoName} />
              <a href={data.repoUrl} rel="noreferrer noopener" target="_blank">
                <p>{data.repoName}</p>
              </a>
            </div>
            <div className="flex items-center mx-2">
              <FaPen className="text-sm text-gray-800 mr-1" />
              <p className="">{data.language}</p>
            </div>
            <div className="text-xs mx-2">
              <p>Sent in for review on:</p>
              <p>{dayjs(data.createdAt).format('MM/DD/YYYY')}</p>
            </div>
          </div>
        </div>
        {user.role === Role.USER && user.id === data.ownerId && (
          <div className="mx-2">
            <button className="text-gray-400 hover:opacity-80">Delete</button>
          </div>
        )}
      </div>
    </li>
  );
};

export default RepositoryReviewListItem;
