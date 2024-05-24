import { useDispatch, useSelector } from 'react-redux';
import { AiOutlineRedEnvelope, AiOutlineUser } from 'react-icons/ai';
import { MdOutlineDriveFileRenameOutline } from 'react-icons/md';

import Header from '../Header';
import FormInputField from '../../Form/FormInputField';
import { TRootState, updateBasicInfoFormField } from '../../../state/store';
import LocationPhoneInput from '../ContactInfo/LocationPhoneInput';

const BasicInfo = () => {
  const dispatch = useDispatch();
  const form = useSelector((store: TRootState) => store.basicInfo);

  const handleUpdateField = (name: string, value: string, attribute: string) => {
    dispatch(updateBasicInfoFormField({ name, value, attribute }));
  };

  const updateContactNumber = (name: string, value: string) => {
    dispatch(updateBasicInfoFormField({ name, value, attribute: 'value' }));
  };

  return (
    <div>
      <Header heading="Basic Info" />
      <div className="my-4">
        <FormInputField
          handleUpdateField={handleUpdateField}
          name={form.fullName.name}
          value={form.fullName.value}
          error={form.fullName.error}
          type={form.fullName.type}
          label="Full Name"
          id="fullName"
          errorField="Full name"
          placeholder="Enter your full name"
          icon={<AiOutlineUser />}
        />
      </div>
      <div className="my-4">
        <FormInputField
          handleUpdateField={handleUpdateField}
          name={form.userName.name}
          value={form.userName.value}
          error={form.userName.error}
          type={form.userName.type}
          label="Username"
          id="userName"
          errorField="Username"
          placeholder="Enter your username"
          icon={<MdOutlineDriveFileRenameOutline />}
        />
      </div>
      <div className="my-4">
        <FormInputField
          handleUpdateField={handleUpdateField}
          name={form.email.name}
          value={form.email.value}
          error={form.email.error}
          type={form.email.type}
          label="Email"
          id="email"
          errorField="Email"
          placeholder="Enter your email"
          icon={<AiOutlineRedEnvelope />}
        />
      </div>
      <div className="my-4">
        <LocationPhoneInput
          value={form.contactNumber.value}
          name={form.contactNumber.name}
          updateField={updateContactNumber}
        />
      </div>
    </div>
  );
};

export default BasicInfo;
