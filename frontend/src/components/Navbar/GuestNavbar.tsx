import { AiOutlineClose } from 'react-icons/ai';
import { RxHamburgerMenu } from 'react-icons/rx';
import { nanoid } from 'nanoid';
import { useDispatch, useSelector } from 'react-redux';
import { TRootState } from '../../state/store';
import NavbarLink from './NavbarLink';
import { openMobile, closeMobile } from '../../state/store';
import { Link } from 'react-router-dom';

const GuestNavbar = () => {
  const dispatch = useDispatch();
  const { isMobileOpen } = useSelector((store: TRootState) => store.navbar);
  const links = [
    { path: 'about', title: 'About' },
    { path: 'community', title: 'Community' },
    { path: 'signin', title: 'Sign In' },
    { path: 'signup', title: 'Sign Up' },
  ];

  return (
    <nav className="flex flex-row justify-between p-2 items-center">
      <Link to="/">
        <h1 className="text-2xl text-green-400 font-bold font-display tracking-wider">OverWatch</h1>
      </Link>
      <div className="flex flex-row-reverse">
        <div
          onClick={() => dispatch(openMobile())}
          className={`md:hidden text-3xl cursor-pointer text-green-400 ${isMobileOpen ? 'hidden' : 'block'}`}
        >
          <RxHamburgerMenu data-testid="guest-hamburger-menu" />
        </div>
        <div
          onClick={() => dispatch(closeMobile())}
          className={`md:hidden text-3xl cursor-pointer text-green-400 ${isMobileOpen ? 'block' : 'hidden'}`}
        >
          <AiOutlineClose data-testid="guest-close-menu" />
        </div>
        <div
          className={`md:bg-inherit md:relative md:block md:p-0 p-2 z-10 shadow-md bg-gray-900 rounded absolute md:top-0 top-16 md:min-h-full min-h-[calc(100vh-10vh)] md:w-full w-60 animate-slidemenu ${
            isMobileOpen ? 'block' : 'hidden'
          }`}
        >
          <ul className="md:flex md:flex-row flex-col">
            {links.map(({ path, title }) => {
              return <NavbarLink key={nanoid()} path={path} title={title} />;
            })}
          </ul>
        </div>
      </div>
    </nav>
  );
};

export default GuestNavbar;
