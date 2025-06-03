import { AiOutlineMail, AiOutlinePhone, AiOutlineUser } from 'react-icons/ai';

export interface IContactInfoProps {
  userName: string;
  email: string;
  contactNumber: string;
}

const ContactInfo = ({ userName, email, contactNumber }: IContactInfoProps) => {
  return (
    <div className="p-4 border border-t-0 border-gray-800">
      <h3 className="text-lg text-gray-400">Contact Info</h3>
      <div className="flex items-center">
        <div className="flex items-center">
          <AiOutlineUser data-testid="react-icon-user" />
          <p className="m-1">{userName}</p>
        </div>
        <div className="flex items-center">
          <AiOutlinePhone data-testid="react-icon-phone" />
          <p className="m-1">{contactNumber}</p>
        </div>
      </div>
      <div className="flex items-center">
        <AiOutlineMail data-testid="react-icon-email" />
        <p className="m-1">{email}</p>
      </div>
    </div>
  );
};

export default ContactInfo;
