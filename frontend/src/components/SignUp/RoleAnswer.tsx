import { useDispatch, useSelector } from 'react-redux';
import { Role } from '../../enums';
import { updateRole } from '../../state/store';
import { TRootState } from '../../state/store';
import { AiOutlineCheck } from 'react-icons/ai';

export interface IRoleAnswerProps {
  answer: string;
  role: Role;
}

const RoleAnswer = ({ answer, role }: IRoleAnswerProps) => {
  const { role: roleState } = useSelector((store: TRootState) => store.signup);
  const dispatch = useDispatch();
  return (
    <>
      {(roleState.value === Role.UNASSIGNED || roleState.value === role) && (
        <div>
          <div
            onClick={() => dispatch(updateRole(role === roleState.value ? Role.UNASSIGNED : role))}
            className={`my-2 ${
              roleState.value === role ? 'bg-green-400 text-black animate-bounce-short' : 'bg-inherit text-inherit'
            } p-2 rounded cursor-pointer`}
          >
            {roleState.value === role && (
              <div className="text-green-700 text-2xl">
                <AiOutlineCheck aria-label="checkmark" />
              </div>
            )}
            <p
              className={`hover:opacity-80 rounded border ${
                roleState.value === role ? 'border-transparent' : 'border-slate-700'
              } p-2`}
            >
              {answer}
            </p>
          </div>
        </div>
      )}
    </>
  );
};

export default RoleAnswer;
