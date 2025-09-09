import dayjs from 'dayjs';
import { IViewUser } from '../../../../../interfaces';
import { initializeName } from '../../../../../util';
import Avatar from '../../../../Shared/Avatar';
import { useSelector } from 'react-redux';
import { TRootState } from '../../../../../state/store';

export interface IUserListItemProps {
  user: IViewUser;
}

const UserListItem = ({ user }: IUserListItemProps) => {
  const { user: currentUser } = useSelector((store: TRootState) => store.user);
  return (
    <div
      data-testid="UserListItem"
      className="my-4 border-gray-800 text-gray-400 rounded border p-2 flex items-center justify-between"
    >
      <div className="flex items-center">
        <div>
          <Avatar
            height="h-9"
            width="w-9"
            initials={initializeName(user.firstName, user.lastName)}
            avatarUrl={user.avatarUrl}
          />
        </div>

        <p className="ml-2 text-sm">
          {user.email} {user.id === currentUser.id ? '(You)' : ''}
        </p>
      </div>
      <div>
        <p className="text-sm">
          {user.firstName} {user.lastName}
        </p>
      </div>
      <div>{user.id}</div>
      <div>
        <span className="mx-2 text-sm">Joined on</span>
        <span className="text-xs text-blue-400">{dayjs(user.createdAt).format('MM/DD/YYYY')}</span>
      </div>
    </div>
  );
};

export default UserListItem;
