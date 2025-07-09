import { useDispatch } from 'react-redux';
import { Link } from 'react-router-dom';
import { clearChat } from '../../state/store';

export interface IDashboardNavigationLinkProps {
  path: string;
  label: string;
  icon: React.ReactNode;
}

const DashboardNavigationLink = ({ path, label, icon }: IDashboardNavigationLinkProps) => {
  const dispatch = useDispatch();
  const handleOnClick = () => {
    if (path.includes('/connects')) {
      dispatch(clearChat());
    }
  };

  return (
    <li onClick={handleOnClick} className="flex items-center my-4">
      <div className="mr-2">{icon}</div>
      <Link data-path={path} to={path}>
        {label}
      </Link>
    </li>
  );
};

export default DashboardNavigationLink;
