import UserOutletContainer from './UserOutletContainer';
import UserSidebarNavigation from './UserSidebarNavigation';

const UserDashboard = () => {
  return (
    <div className="md:max-w-[1450px] w-full mx-auto mt-8">
      <div className="md:flex bg-gray-900 p-2 rounded">
        <UserSidebarNavigation />
        <UserOutletContainer />
      </div>
    </div>
  );
};

export default UserDashboard;
