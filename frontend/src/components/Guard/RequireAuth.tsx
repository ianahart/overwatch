import { useLocation, Navigate } from 'react-router-dom';
import { retrieveTokens } from '../../util';
interface Props {
  children: JSX.Element;
}

const RequireAuth: React.FC<Props> = ({ children }): JSX.Element => {
  const location = useLocation();

  if (retrieveTokens()?.token) {
    return children;
  } else {
    return <Navigate to="/" replace state={{ path: location.pathname }} />;
  }
};

export default RequireAuth;
