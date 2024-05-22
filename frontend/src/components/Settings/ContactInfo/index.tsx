import Header from '../Header';
import Account from './Account';
import Location from './Location';

const ContactInfo = () => {
  return (
    <div className="p-4">
      <Header heading="Contact Info" />
      <div className="my-4">
        <Account />
      </div>
      <div className="my-4">
        <Location />
      </div>
    </div>
  );
};

export default ContactInfo;
