import dayjs from 'dayjs';
import { AiFillStar } from 'react-icons/ai';
import { Link } from 'react-router-dom';

import { ITeamMember } from '../../../interfaces';
import Avatar from '../../Shared/Avatar';
import { initializeName } from '../../../util';

export interface ITeamMemberItemProps {
  teamMember: ITeamMember;
  isAdmin: boolean;
}

const TeamMemberItem = ({ teamMember, isAdmin }: ITeamMemberItemProps) => {
  const [first, last] = teamMember.fullName.split(' ');
  return (
    <div className="my-4">
      <div className="flex items-center">
        <Avatar width="w-9" height="h-9" avatarUrl={teamMember.avatarUrl} initials={initializeName(first, last)} />
        <Link to={`/profiles/${teamMember.profileId}`}>
          <div className="ml-1">
            <p>{teamMember.fullName}</p>
            <p className={`${isAdmin ? 'text-green-400' : 'text-blue-400'} text-xs flex items-center`}>
              <AiFillStar className="mr-1" />
              {isAdmin ? 'Admin' : 'Team Member'}
            </p>
            <p className="text-xs">Joined {dayjs(teamMember.createdAt).format('MM/DD/YYYY')}</p>
          </div>
        </Link>
      </div>
    </div>
  );
};

export default TeamMemberItem;
