import { Link, useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { IoTelescopeOutline, IoSettingsOutline, IoPeopleOutline } from 'react-icons/io5';

import Avatar from '../Shared/Avatar';
import { TRootState, clearSetting, clearUser, useSignOutMutation } from '../../state/store';
import { openMobile, closeMobile } from '../../state/store';
import UserInfo from './UserInfo';
import { AiOutlineClose, AiOutlineLogout } from 'react-icons/ai';
import { nanoid } from 'nanoid';
import Notifications from '../Notification';
import { MdOutlineSpaceDashboard } from 'react-icons/md';
import { Session } from '../../util/SessionService';

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
        dispatch(clearSetting());
        Session.removeItem('github_access_token');
        localStorage.clear();
        navigate('/signin');
      });
  };

  const links = [
    { path: `/dashboard/${user.slug}`, title: 'Dashboard', icon: <MdOutlineSpaceDashboard /> },
    { path: 'explore/most-recent', title: 'Explore', icon: <IoTelescopeOutline /> },
    { path: 'community', title: 'Community', icon: <IoPeopleOutline /> },
    { path: `settings/${user.slug}/profile`, title: 'Settings', icon: <IoSettingsOutline /> },
  ];

  return (
    <nav className="flex justify-between p-1">
      <h1 className="text-2xl text-green-400 font-bold font-display tracking-wider">OverWatch</h1>
      <div className="relative cursor-pointer">
        <div className="flex items-center">
          {user.loggedIn && <Notifications />}
          <div onClick={() => dispatch(openMobile())}>
            <Avatar initials={user.abbreviation} avatarUrl={user.avatarUrl} width="w-10" height="h-10" />
          </div>
        </div>
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
                  <li
                    key={nanoid()}
                    onClick={(e) => {
                      e.stopPropagation();
                      dispatch(closeMobile());
                    }}
                    className="flex items-center my-4"
                  >
                    {link.icon}
                    <Link className="ml-2" to={link.path}>
                      {link.title}
                    </Link>
                  </li>
                );
              })}
              <li className="flex items-center my-4">
                <AiOutlineLogout />
                <button className="ml-2" onClick={handleClick}>
                  Logout
                </button>
              </li>
            </ul>
          </div>
        )}
      </div>
    </nav>
  );
};

export default AuthNavbar;
