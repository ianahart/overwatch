import { useParams } from 'react-router-dom';
import Profile from '../components/Profile';

const ExploreProfileRoute = () => {
  const params = useParams();
  const profileId = params.profileId as string;

  return <Profile profileId={parseInt(profileId)} />;
};

export default ExploreProfileRoute;
