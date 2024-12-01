import { IPaginationState, ITeamComment } from '../../../../interfaces';
import TeamCommentListItem from './TeamCommentListItem';

export interface ITeamCommentListProps {
  teamComments: ITeamComment[];
  paginateTeamComments: (dir: string, reset?: boolean) => void;
  pag: IPaginationState;
  updateTeamComment: (teamCommentId: number, content: string, tag: string) => void;
  handleResetComments: () => void;
}

const TeamCommentList = ({
  teamComments,
  paginateTeamComments,
  pag,
  updateTeamComment,
  handleResetComments,
}: ITeamCommentListProps) => {
  return (
    <div className="my-4 h-36 overflow-y-auto">
      {teamComments.map((teamComment) => {
        return (
          <TeamCommentListItem
            key={teamComment.id}
            teamComment={teamComment}
            updateTeamComment={updateTeamComment}
            handleResetComments={handleResetComments}
          />
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
