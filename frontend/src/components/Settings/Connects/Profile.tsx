import { useSelector } from 'react-redux';
import { TRootState } from '../../../state/store';
import Avatar from '../../Shared/Avatar';
import { AiOutlineMail, AiOutlinePhone, AiOutlineUser } from 'react-icons/ai';

const Profile = () => {
  const { currentConnection } = useSelector((store: TRootState) => store.chat);
  return (
    <div className="my-8 flex flex-col items-center">
      <Avatar width="w-26" height="h-26" initials="?.?" avatarUrl={currentConnection.avatarUrl} />
      <div className="my-4">
        <p className="text-gray-400">
          {currentConnection.firstName} {currentConnection.lastName}
        </p>
      </div>
      <div className="flex items-center my-2">
        <AiOutlineMail data-testid="settings-profile-mail-icon" className="mr-2" />
        <p>{currentConnection.email}</p>
      </div>
      <div className="flex items-center my-2">
        <AiOutlinePhone data-testid="settings-profile-phone-icon" className="mr-2" />
        <p>{currentConnection.phoneNumber}</p>
      </div>
      <div className="flex items-center my-2">
        <AiOutlineUser data-testid="settings-profile-user-icon" className="mr-2" />
        <p>{currentConnection.bio}</p>
      </div>
    </div>
  );
};

export default Profile;
