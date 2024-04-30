import NavbarLink from './NavbarLink';
import { RxHamburgerMenu } from 'react-icons/rx';
import { nanoid } from 'nanoid';
import { useDispatch, useSelector } from 'react-redux';
import { TRootState } from '../../state/store';
import { openMobile, closeMobile } from '../../state/navbar/navbarSlice';
import { AiOutlineClose } from 'react-icons/ai';

const Navbar = () => {
  const { isMobileOpen } = useSelector((store: TRootState) => store.navbar);
  const dispatch = useDispatch();

  const links = [
    { path: 'about', title: 'About', showLoggedIn: true },
    { path: 'explore', title: 'Explore', showLoggedIn: true },
    { path: 'community', title: 'Community', showLoggedIn: true },
    { path: 'signin', title: 'Sign In', showLoggedIn: false },
    { path: 'signup', title: 'Sign Up', showLoggedIn: false },
  ];

  return (
    <section className="p-1">
      <nav className="flex flex-row justify-between p-2 items-center">
        <h1 className="text-2xl text-green-400 font-bold font-display tracking-wider">OverWatch</h1>
        <div className="flex flex-row-reverse">
          <div
            onClick={() => dispatch(openMobile())}
            className={`md:hidden text-3xl cursor-pointer text-green-400 ${isMobileOpen ? 'hidden' : 'block'}`}
          >
            <RxHamburgerMenu />
          </div>
          <div
            onClick={() => dispatch(closeMobile())}
            className={`md:hidden text-3xl cursor-pointer text-green-400 ${isMobileOpen ? 'block' : 'hidden'}`}
          >
            <AiOutlineClose />
          </div>
          <div
            className={`md:bg-inherit md:relative md:block md:p-0 p-2 shadow bg-gray-900 rounded absolute md:top-0 top-16 md:min-h-full min-h-[calc(100vh-10vh)] md:w-full w-60 animate-slidemenu ${
              isMobileOpen ? 'block' : 'hidden'
            }`}
          >
            {/*USER INFO HERE WHEN LOGGED IN*/}
            <ul className="md:flex md:flex-row flex-col">
              {links.map(({ path, title, showLoggedIn }) => {
                return <NavbarLink key={nanoid()} path={path} title={title} showLoggedIn={showLoggedIn} />;
              })}
            </ul>
          </div>
        </div>
      </nav>
    </section>
  );
};

export default Navbar;
