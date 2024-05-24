import Header from '../Header';
import EditProfileForm from './EditProfileForm';

const EditProfile = () => {
  return (
    <div className="p-4">
      <Header heading="Edit Profile" />
      <div className="my-4">
        <EditProfileForm />
      </div>
    </div>
  );
};

export default EditProfile;
