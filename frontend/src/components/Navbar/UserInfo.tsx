import { IUser } from '../../interfaces';
import Avatar from '../Shared/Avatar';

export interface IUserInfoProps {
  user: IUser;
}

const UserInfo = ({ user }: IUserInfoProps) => {
  return (
    <div className="my-4 flex items-center">
      <div className="mr-2">
        <Avatar initials={user.abbreviation} avatarUrl={user.avatarUrl} width="w-10" height="h-10" />
      </div>
      <div>
        <p className="text-slate-400 text-sm font-bold">{user.fullName}</p>
      </div>
    </div>
  );
};

export default UserInfo;
