import { AiOutlineClose } from 'react-icons/ai';
import { RxHamburgerMenu } from 'react-icons/rx';
import { nanoid } from 'nanoid';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { TRootState, clearUser, useSignOutMutation } from '../../state/store';
import NavbarLink from './NavbarLink';
import { openMobile, closeMobile } from '../../state/store';
import UserInfo from './UserInfo';

const Navbar = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [signOut] = useSignOutMutation();
  const { isMobileOpen } = useSelector((store: TRootState) => store.navbar);
  const { user, token, refreshToken } = useSelector((store: TRootState) => store.user);

  const handleClick = () => {
    signOut({ token, refreshToken })
      .unwrap()
      .then(() => {
        dispatch(clearUser());
        dispatch(closeMobile());
        navigate('/signin');
      });
  };

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
            className={`md:bg-inherit md:relative md:block md:p-0 p-2 z-10 shadow-md bg-gray-900 rounded absolute md:top-0 top-16 md:min-h-full min-h-[calc(100vh-10vh)] md:w-full w-60 animate-slidemenu ${
              isMobileOpen ? 'block' : 'hidden'
            }`}
          >
            {isMobileOpen && user.loggedIn && <UserInfo user={user} />}
            <ul className="md:flex md:flex-row flex-col">
              {links.map(({ path, title, showLoggedIn }) => {
                return <NavbarLink key={nanoid()} path={path} user={user} title={title} showLoggedIn={showLoggedIn} />;
              })}
              {user.loggedIn && (
                <li className="p-2">
                  <button onClick={handleClick}>Logout</button>
                </li>
              )}
            </ul>
          </div>
        </div>
      </nav>
    </section>
  );
};

export default Navbar;
