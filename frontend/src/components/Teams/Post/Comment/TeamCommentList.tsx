import dayjs from 'dayjs';
import { IPaginationState, ITeamComment } from '../../../../interfaces';
import Avatar from '../../../Shared/Avatar';
import { initializeName } from '../../../../util';

export interface ITeamCommentListProps {
  teamComments: ITeamComment[];
  paginateTeamComments: (dir: string, reset?: boolean) => void;
  pag: IPaginationState;
}

const TeamCommentList = ({ teamComments, paginateTeamComments, pag }: ITeamCommentListProps) => {
  console.log(teamComments);
  return (
    <div className="my-4 h-36 overflow-y-auto">
      {teamComments.map((teamComment) => {
        return (
          <div key={teamComment.id} className="border border-gray-800 rounded p-2 my-2">
            <div className="flex items-center">
              <Avatar
                height="h-9"
                width="w-9"
                avatarUrl={teamComment.avatarUrl}
                initials={initializeName(teamComment.fullName.split(' ')[0], teamComment.fullName.split(' ')[1])}
              />
              <div className="ml-2">
                {teamComment.isEdited && <p className="text-blue-400 text-xs">(edited)</p>}
                <p className="text-xs">{teamComment.fullName}</p>
                <p className="text-xs">{dayjs(teamComment.createdAt).format('MM/DD/YYYY')}</p>
              </div>
            </div>
            <div className="my-1 p-2">{teamComment.content}</div>
          </div>
        );
      })}
      {pag.page < pag.totalPages - 1 && (
        <div className="my-4">
          <button onClick={() => paginateTeamComments('next', false)}>Read more...</button>
        </div>
      )}
    </div>
  );
};

export default TeamCommentList;
