import { useSelector } from 'react-redux';
import Header from '../Header';
import BlockedUserList from './BlockedUserList';
import ProfileVisibility from './ProfileVisibility';
import { TRootState } from '../../../state/store';

const ProfileSettings = () => {
  const { user } = useSelector((store: TRootState) => store.user);

  return (
    <div className="p-4">
      <Header heading="Profile Settings" />
      <div className="my-4">
        <BlockedUserList />
      </div>
      {user.role === 'REVIEWER' && (
        <div className="my-4">
          <ProfileVisibility />
        </div>
      )}
    </div>
  );
};

export default ProfileSettings;
