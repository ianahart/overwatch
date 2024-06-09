import { useSelector } from 'react-redux';
import Profile from '../../components/Profile';
import { TRootState } from '../../state/store';

const ProfileRoute = () => {
  const { user } = useSelector((store: TRootState) => store.user);

  return <Profile profileId={user.profileId} />;
};

export default ProfileRoute;
