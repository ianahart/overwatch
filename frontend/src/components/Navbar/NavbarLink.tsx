import { NavLink } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { closeMobile } from '../../state/store';

interface INavbarLinkProps {
  path: string;
  title: string;
}

const NavbarLink = ({ path, title }: INavbarLinkProps) => {
  const dispatch = useDispatch();

  const handleClick = () => {
    dispatch(closeMobile());
  };

  return (
    <>
      <li className="md:mx-2 md:p-0 p-2 hover:bg-gray-800 transition ease-in-out rounded">
        <NavLink onClick={handleClick} to={path}>
          {title}
        </NavLink>
      </li>
    </>
  );
};

export default NavbarLink;
