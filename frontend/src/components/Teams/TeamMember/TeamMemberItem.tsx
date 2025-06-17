import dayjs from 'dayjs';
import { AiFillStar } from 'react-icons/ai';
import { Link } from 'react-router-dom';
import { BsTrash } from 'react-icons/bs';
import { useSelector } from 'react-redux';

import { ITeamMember } from '../../../interfaces';
import Avatar from '../../Shared/Avatar';
import { initializeName } from '../../../util';
import ToolTip from '../../Shared/ToolTip';
import { TRootState } from '../../../state/store';

export interface ITeamMemberItemProps {
  teamMember: ITeamMember;
  adminUserId: number;
  isAdmin: boolean;
  handleDeleteTeamMember?: (teamMemberId: number) => void;
}

const TeamMemberItem = ({
  teamMember,
  isAdmin,
  adminUserId,
  handleDeleteTeamMember = () => {},
}: ITeamMemberItemProps) => {
  const { user } = useSelector((store: TRootState) => store.user);
  const [first, last] = teamMember.fullName.split(' ');

  const handleOnClick = (): void => {
    handleDeleteTeamMember(teamMember.id);
  };

  return (
    <div data-testid={`${isAdmin ? 'team-member-admin' : 'team-member-item'}`} className="my-4">
      <div className="flex items-center">
        <Avatar width="w-9" height="h-9" avatarUrl={teamMember.avatarUrl} initials={initializeName(first, last)} />
        <div className="ml-1">
          <Link to={`/profiles/${teamMember.profileId}`}>
            <p>{teamMember.fullName}</p>
          </Link>
          <div className="flex items-center">
            <p className={`${isAdmin ? 'text-green-400' : 'text-blue-400'} text-xs flex items-center`}>
              <AiFillStar className="mr-1" />
              {isAdmin ? 'Admin' : 'Team Member'}
            </p>

            {user.id === adminUserId && !isAdmin && (
              <ToolTip message="Remove member">
                <BsTrash data-testid="remove-team-member-icon" onClick={handleOnClick} className="text-sm ml-2" />
              </ToolTip>
            )}
          </div>
          <p className="text-xs">Joined {dayjs(teamMember.createdAt).format('MM/DD/YYYY')}</p>
        </div>
      </div>
    </div>
  );
};

export default TeamMemberItem;
