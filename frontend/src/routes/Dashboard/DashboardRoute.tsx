import { useSelector } from 'react-redux';
import { TRootState } from '../../state/store';
import { Role } from '../../enums';
import ReviewerDashboardRoute from './ReviewerDashboardRoute';
import UserDashboardRoute from './UserDashboardRoute';

const DashboardRoute = () => {
  const { user } = useSelector((store: TRootState) => store.user);

  return user.role === Role.REVIEWER ? <ReviewerDashboardRoute /> : <UserDashboardRoute />;
};

export default DashboardRoute;
