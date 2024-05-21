import { IAccountForm } from '../../../interfaces';
import { maskEmail } from '../../../util';

interface IAccountInfoProps {
  userId: number;
  email: string;
  lastName: string;
  firstName: string;
}

const AccountInfo = ({ email, firstName, lastName, userId }: IAccountInfoProps) => {
  return (
    <div className="my-4">
      <div className="my-4">
        <p>User ID</p>
        <p>{userId}</p>
        <div className="my-4">
          <p>First Name</p>
          <p>{firstName}</p>
        </div>
        <div className="my-4">
          <p>Last Name</p>
          <p>{lastName}</p>
        </div>
        <div className="my-4">
          <p>Email</p>
          <p>{maskEmail(email)}</p>
        </div>
      </div>
    </div>
  );
};

export default AccountInfo;
