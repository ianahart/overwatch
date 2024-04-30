import { NavLink } from 'react-router-dom';

interface INavbarLinkProps {
  path: string;
  title: string;
  showLoggedIn: boolean;
}

const NavbarLink = ({ path, title, showLoggedIn }: INavbarLinkProps) => {
  return (
    <li className="md:mx-2 md:p-0 p-2 hover:bg-gray-800 transition ease-in-out rounded">
      <NavLink to={path}>{title}</NavLink>
    </li>
  );
};

export default NavbarLink;
