import { NavLink } from 'react-router-dom';
import { IUser } from '../../interfaces';

interface INavbarLinkProps {
  user: IUser;
  path: string;
  title: string;
  showLoggedIn: boolean;
}

const NavbarLink = ({ user, path, title, showLoggedIn }: INavbarLinkProps) => {
  return (
    <>
      {(showLoggedIn || !user.loggedIn) && (
        <li className="md:mx-2 md:p-0 p-2 hover:bg-gray-800 transition ease-in-out rounded">
          <NavLink to={path}>{title}</NavLink>
        </li>
      )}
    </>
  );
};

export default NavbarLink;
