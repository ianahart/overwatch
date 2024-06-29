import { useDispatch, useSelector } from 'react-redux';
import { Link, useLocation } from 'react-router-dom';
import { TRootState, clearChat } from '../../state/store';

export interface INavigationLinkProps {
  data: { path: string; name: string };
}

const NavigationLink = ({ data }: INavigationLinkProps) => {
  const dispatch = useDispatch();
  const { connections } = useSelector((store: TRootState) => store.chat);

  const handleOnClick = () => {
    if (connections.length) {
      dispatch(clearChat());
    }
  };

  const location = useLocation();
  const { path, name } = data;
  const activeLink = location.pathname === path ? 'border-green-400 my-2' : 'border-gray-400';
  const activeText = location.pathname === path ? 'text-green-400 font-bold' : 'text-inherit';

  return (
    <li onClick={handleOnClick} className={`py-2 border-l pl-2 ${activeLink} ${activeText}`}>
      <Link to={path}>{name}</Link>
    </li>
  );
};

export default NavigationLink;
