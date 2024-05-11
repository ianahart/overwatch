import { Link, useLocation } from 'react-router-dom';

export interface INavigationLinkProps {
  data: { path: string; name: string };
}

const NavigationLink = ({ data }: INavigationLinkProps) => {
  const location = useLocation();
  const { path, name } = data;
  const activeLink = location.pathname === path ? 'border-green-400 my-2' : 'border-gray-400';
  const activeText = location.pathname === path ? 'text-green-400 font-bold' : 'text-inherit';

  return (
    <li className={`py-2 border-l pl-2 ${activeLink} ${activeText}`}>
      <Link to={path}>{name}</Link>
    </li>
  );
};

export default NavigationLink;
