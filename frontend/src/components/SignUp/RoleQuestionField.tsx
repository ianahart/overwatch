import { Role } from '../../enums';
import RoleAnswer from './RoleAnswer';
const RoleQuestionField = () => {
  return (
    <div>
      <RoleAnswer role={Role.USER} answer="I am looking for someone to review my code" />
      <RoleAnswer role={Role.REVIEWER} answer="I am looking to review the code of others" />
    </div>
  );
};

export default RoleQuestionField;
