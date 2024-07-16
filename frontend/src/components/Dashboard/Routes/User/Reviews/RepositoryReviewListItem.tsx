import dayjs from 'dayjs';
import { useMemo } from 'react';
import { FaPen } from 'react-icons/fa';
import { AiOutlineEdit } from 'react-icons/ai';
import { BsTrash } from 'react-icons/bs';
import { useDispatch, useSelector } from 'react-redux';

import { TRootState, clearRepositoryReviews, useDeleteUserRepositoryMutation } from '../../../../../state/store';
import { IRepositoryReview } from '../../../../../interfaces';
import { Role } from '../../../../../enums';
import DashboardAvatar from '../../../DashboardAvatar';
import { Link } from 'react-router-dom';

export interface IRepositoryReviewListItemProps {
  data: IRepositoryReview;
}

export interface IProgressMapper {
  INCOMPLETE: { text: string; background: string };
  INPROGRESS: { text: string; background: string };
  COMPLETED: { text: string; background: string };
}

const RepositoryReviewListItem = ({ data }: IRepositoryReviewListItemProps) => {
  const [deleteRepository] = useDeleteUserRepositoryMutation();
  const { user, token } = useSelector((store: TRootState) => store.user);
  const dispatch = useDispatch();
  const abbreviation = useMemo(() => {
    return data.firstName[0] + '.' + data.lastName[0];
  }, [data.firstName, data.lastName]);

  const handleOnDeleteRepository = async () => {
    try {
      await deleteRepository({ token, repositoryId: data.id }).unwrap();
      dispatch(clearRepositoryReviews());
    } catch (err) {
      const error = err as string;
      throw new Error(error);
    }
  };

  const progressMapper: IProgressMapper = {
    INCOMPLETE: { text: 'Incomplete', background: 'bg-gray-400' },
    INPROGRESS: { text: 'In progress', background: 'bg-blue-400' },
    COMPLETED: { text: 'Completed', background: 'bg-green-400' },
  };

  const progressStatus = data.status as keyof IProgressMapper;

  return (
    <li className="my-4 border p-1 rounded border-gray-800">
      <div className="flex items-center justify-between">
        <div className="items-end flex">
          <div>
            <p
              className={`md:flex hidden flex-col w-20 my-1 text-black p-1 text-xs rounded ${progressMapper[progressStatus].background}`}
            >
              {progressMapper[progressStatus].text}
            </p>
            <div className="flex items-center">
              <DashboardAvatar abbreviation={abbreviation} url={data.profileUrl} height="h-9" width="w-9" />
              <div className="ml-2">
                <p className="text-gray-400 text-sm">
                  {data.firstName} {data.lastName}
                </p>
              </div>
            </div>
          </div>
          <div className="text-sm flex items-center m-2 text-gray-400">
            <div className="flex items-center mx-2">
              <img className="rounded-full w-6 h-6 mr-1" src={data.avatarUrl} alt={data.repoName} />
              <a href={data.repoUrl} rel="noreferrer noopener" target="_blank">
                <p>{data.repoName}</p>
              </a>
            </div>
            <div className="md:flex hidden items-center mx-2">
              <FaPen className="text-sm text-gray-800 mr-1" />
              <p className="">{data.language}</p>
            </div>
          </div>
        </div>
        {user.role === Role.USER && user.id === data.ownerId && (
          <div className="md:flex hidden items-end justify-between">
            <div className="text-xs mx-2">
              <p>Sent in for review on:</p>
              <p>{dayjs(data.createdAt).format('MM/DD/YYYY')}</p>
            </div>

            <div className="flex items-center">
              <Link to={`/dashboard/${user.slug}/user/reviews/${data.id}/edit`}>
                <AiOutlineEdit className="mx-2 text-lg cursor-pointer" />
              </Link>
              <BsTrash onClick={handleOnDeleteRepository} className="max-2 text-lg cursor-pointer" />
            </div>
          </div>
        )}
      </div>
    </li>
  );
};

export default RepositoryReviewListItem;
