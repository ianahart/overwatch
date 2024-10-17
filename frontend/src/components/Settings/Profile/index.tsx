import Header from '../Header';
import BlockedUserList from './BlockedUserList';

const ProfileSettings = () => {
  return (
    <div className="p-4">
      <Header heading="Profile Settings" />
      <div className="my-4">
        <BlockedUserList />
      </div>
    </div>
  );
};

export default ProfileSettings;
