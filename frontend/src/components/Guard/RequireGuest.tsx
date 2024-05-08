import { useLocation, Navigate } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { TRootState } from '../../state/store';
import { retrieveTokens } from '../../util';
interface Props {
  children: JSX.Element;
}

const RequireGuest: React.FC<Props> = ({ children }) => {
  const { user } = useSelector((store: TRootState) => store.user);

  const location = useLocation();
  const guestRoutes = ['/', '/signin', '/signup', '/forgot-password', '/reset-password'];
  const storage = retrieveTokens();
  if (storage === undefined && guestRoutes.includes(location.pathname)) {
    return children;
  } else {
    if (user.id !== 0) {
      return <Navigate to="/" replace state={{ path: location.pathname }} />;
    }
  }
};

export default RequireGuest;
