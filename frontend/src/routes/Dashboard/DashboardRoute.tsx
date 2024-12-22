import { useSelector } from 'react-redux';
import { TRootState } from '../../state/store';
import ReviewerDashboardRoute from './ReviewerDashboardRoute';
import UserDashboardRoute from './UserDashboardRoute';
import AdminDashboardRoute from './AdminDashboardRoute';

const DashboardRoute = () => {
  const { user } = useSelector((store: TRootState) => store.user);

  const chooseRoleBasedDashboard = () => {
    switch (user.role) {
      case 'USER':
        return <UserDashboardRoute />;
      case 'REVIEWER':
        return <ReviewerDashboardRoute />;
      case 'ADMIN':
        return <AdminDashboardRoute />;
      default:
        return <>Loading...</>;
    }
  };

  return <>{chooseRoleBasedDashboard()}</>;
};

export default DashboardRoute;
