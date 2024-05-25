import { useDispatch, useSelector } from 'react-redux';
import { BsPencil } from 'react-icons/bs';
import Header from '../Header';
import AvatarUpload from './AvatarUpload';
import { TRootState, updateProfileSetupFormField } from '../../../state/store';
import FormInputField from '../../Form/FormInputField';
import FormTextareaField from '../../Form/FormTextareaField';

const ProfileSetup = () => {
  const form = useSelector((store: TRootState) => store.profileSetup);
  const dispatch = useDispatch();

  const handleUpdateField = (name: string, value: string, attribute: string) => {
    dispatch(updateProfileSetupFormField({ name, value, attribute }));
  };

  return (
    <>
      <Header heading="Profile Setup" />
      <AvatarUpload />
      <div className="my-4">
        <FormInputField
          handleUpdateField={handleUpdateField}
          name={form.tagLine.name}
          value={form.tagLine.value}
          error={form.tagLine.error}
          type={form.tagLine.type}
          label="Tag line or title"
          id="tagLine"
          errorField="Tag line"
          placeholder="Enter your tag line"
          icon={<BsPencil />}
        />
      </div>
      <div className="my-4">
        <FormTextareaField
          handleUpdateField={handleUpdateField}
          name={form.bio.name}
          value={form.bio.value}
          error={form.bio.error}
          label="Bio"
          id="bio"
          errorField="Bio"
          placeholder="Enter a short Bio"
          max={400}
        />
      </div>
    </>
  );
};

export default ProfileSetup;
