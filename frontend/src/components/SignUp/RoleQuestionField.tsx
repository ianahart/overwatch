import { Role } from '../../enums';
import RoleAnswer from './RoleAnswer';
import { PiBaseballCap } from 'react-icons/pi';

const RoleQuestionField = () => {
  return (
    <div className="mt-2">
      <div className="flex items-center">
        <PiBaseballCap className="mr-2" />
        <p className="text-gray-400">What best describes you?</p>
      </div>
      <RoleAnswer role={Role.USER} answer="I am looking for someone to review my code" />
      <RoleAnswer role={Role.REVIEWER} answer="I am looking to review the code of others" />
    </div>
  );
};

export default RoleQuestionField;
