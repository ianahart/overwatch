import { useSelector } from 'react-redux';

import DashboardAvatar from './DashboardAvatar';
import { TRootState } from '../../state/store';
import { Outlet } from 'react-router-dom';

const UserOutletContainer = () => {
  const { user } = useSelector((store: TRootState) => store.user);

  return (
    <div className="rounded bg-gray-950 w-full md:flex-grow-[2] min-h-[90vh]">
      <div className="p-4 flex items-center justify-end">
        <div>
          <h3 className="mr-2 font-bold">
            {user.firstName} {user.lastName}
          </h3>
          <p className="mr-2 text-sm">{user.email}</p>
        </div>
        <DashboardAvatar width="w-12" height="h-12" abbreviation={user.abbreviation} url={user.avatarUrl} />
      </div>
      <div className="my-8 p-4">
        <Outlet />
      </div>
    </div>
  );
};

export default UserOutletContainer;
