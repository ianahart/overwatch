import Header from '../Header';
import ChangePassword from './ChangePassword';
import DeleteAccount from './DeleteAccount';
import MultiFactorAuthentication from './MultiFactorAuthentication';

const Security = () => {
  return (
    <div className="p-4">
      <Header heading="Password & Security" />
      <ChangePassword />
      <MultiFactorAuthentication />
      <DeleteAccount />
    </div>
  );
};

export default Security;
