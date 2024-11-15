import { useSelector } from 'react-redux';
import { TRootState } from '../../state/store';
import GuestNavbar from './GuestNavbar';
import AuthNavbar from './AuthNavbar';
import AdminNavbar from './AdminNavbar';

const Navbar = () => {
  const { user } = useSelector((store: TRootState) => store.user);

  const chooseNavbar = (): JSX.Element => {
    if (user.loggedIn && (user.role === 'USER' || user.role === 'REVIEWER')) {
      return <AuthNavbar />;
    } else if (user.loggedIn && user.role === 'ADMIN') {
      return <AdminNavbar />;
    } else {
      return <GuestNavbar />;
    }
  };

  return <section className="p-1">{chooseNavbar()}</section>;
};

export default Navbar;
