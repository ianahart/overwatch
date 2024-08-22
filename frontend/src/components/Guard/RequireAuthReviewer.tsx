import { useLocation, Navigate } from 'react-router-dom';
import { retrieveTokens } from '../../util';
import { useSelector } from 'react-redux';
import { TRootState } from '../../state/store';
import { Role } from '../../enums';
interface Props {
  children: JSX.Element;
}

const RequireAuthReviewer: React.FC<Props> = ({ children }): JSX.Element => {
  const location = useLocation();
  const { user } = useSelector((store: TRootState) => store.user);

  if (!user.loggedIn) {
    return <p>loading</p>;
  }

  if (retrieveTokens()?.token && user.role === Role.REVIEWER) {
    return children;
  } else {
    return <Navigate to="/" replace state={{ path: location.pathname }} />;
  }
};

export default RequireAuthReviewer;
