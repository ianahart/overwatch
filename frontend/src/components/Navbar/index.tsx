import { useSelector } from 'react-redux';
import { TRootState } from '../../state/store';
import GuestNavbar from './GuestNavbar';
import AuthNavbar from './AuthNavbar';

const Navbar = () => {
  const { user } = useSelector((store: TRootState) => store.user);

  return <section className="p-1">{!user.loggedIn ? <GuestNavbar /> : <AuthNavbar />}</section>;
};

export default Navbar;
