import { NavLink } from 'react-router-dom';
import NavbarLink from './NavbarLink';
import { RxHamburgerMenu } from 'react-icons/rx';
import { nanoid } from 'nanoid';

const Navbar = () => {
  const links = [
    { path: 'about', title: 'About' },
    { path: 'explore', title: 'Explore' },
    { path: 'community', title: 'Community' },
    { path: 'signin', title: 'Sign In' },
    { path: 'signup', title: 'Sign Up' },
  ];

  return (
    <section className="p-1">
      <nav className="flex flex-row justify-between">
        <h1 className="text-2xl text-green-400 font-bold font-display tracking-wider">OverWatch</h1>
        <div className="md:hidden text-3xl cursor-pointer">
          <RxHamburgerMenu />
        </div>
        <ul className="md:flex md:flex-row flex-col hidden">
          {links.map(({ path, title }) => {
            return <NavbarLink key={nanoid()} path={path} title={title} />;
          })}
        </ul>
      </nav>
    </section>
  );
};

export default Navbar;
