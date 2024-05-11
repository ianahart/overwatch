import { Link, useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { IoTelescopeOutline, IoSettingsOutline, IoPeopleOutline } from 'react-icons/io5';

import Avatar from '../Shared/Avatar';
import NavbarLink from './NavbarLink';
import { TRootState, clearUser, useSignOutMutation } from '../../state/store';
import { openMobile, closeMobile } from '../../state/store';
import UserInfo from './UserInfo';
import { AiOutlineClose } from 'react-icons/ai';

const AuthNavbar = () => {
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
    { path: 'explore', title: 'Explore', icon: <IoTelescopeOutline /> },
    { path: 'community', title: 'Community', icon: <IoPeopleOutline /> },
    { path: `settings/${user.slug}`, title: 'Settings', icon: <IoSettingsOutline /> },
  ];

  return (
    <nav className="flex justify-between p-1">
      <h1 className="text-2xl text-green-400 font-bold font-display tracking-wider">OverWatch</h1>
      <div onClick={() => dispatch(openMobile())} className="relative cursor-pointer">
        <Avatar initials={user.abbreviation} avatarUrl={user.avatarUrl} width="w-10" height="h-10" />
        {isMobileOpen && (
          <div className="p-2 z-10 shadow-md bg-gray-900 rounded absolute top-2  min-h-[calc(100vh-10vh)] w-60 animate-slidemenu right-3">
            <div className="flex justify-end">
              <div
                onClick={(e) => {
                  e.stopPropagation();
                  dispatch(closeMobile());
                }}
              >
                <AiOutlineClose className="text-green-400 text-2xl" />
              </div>
            </div>
            <UserInfo user={user} />
            <ul>
              {links.map((link) => {
                return (
                  <li className="flex items-center my-4">
                    {link.icon}
                    <Link className="ml-2" to={link.path}>
                      {link.title}
                    </Link>
                  </li>
                );
              })}
            </ul>
          </div>
        )}
      </div>
    </nav>
  );
};

export default AuthNavbar;
