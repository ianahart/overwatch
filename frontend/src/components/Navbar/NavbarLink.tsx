import { NavLink } from 'react-router-dom';
import { IUser } from '../../interfaces';
import { useDispatch } from 'react-redux';
import { closeMobile } from '../../state/store';

interface INavbarLinkProps {
  user: IUser;
  path: string;
  title: string;
  showLoggedIn: boolean;
}

const NavbarLink = ({ user, path, title, showLoggedIn }: INavbarLinkProps) => {
  const dispatch = useDispatch();

  const handleClick = () => {
    dispatch(closeMobile());
  };

  return (
    <>
      {(showLoggedIn || !user.loggedIn) && (
        <li className="md:mx-2 md:p-0 p-2 hover:bg-gray-800 transition ease-in-out rounded">
          <NavLink onClick={handleClick} to={path}>
            {title}
          </NavLink>
        </li>
      )}
    </>
  );
};

export default NavbarLink;
