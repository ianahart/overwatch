import Header from '../Header';
import ChangePassword from './ChangePassword';
import MultiFactorAuthentication from './MultiFactorAuthentication';

const Security = () => {
  return (
    <div className="p-4">
      <Header heading="Password & Security" />
      <ChangePassword />
      <MultiFactorAuthentication />
    </div>
  );
};

export default Security;
