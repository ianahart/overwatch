import { NavLink } from 'react-router-dom';

interface INavbarLinkProps {
  path: string;
  title: string;
}

const NavbarLink = ({ path, title }: INavbarLinkProps) => {
  return (
    <li className="md:mx-2">
      <NavLink to={path}>{title}</NavLink>
    </li>
  );
};

export default NavbarLink;
