import dayjs from 'dayjs';
import { BsTrash } from 'react-icons/bs';
import { useState } from 'react';
import { useSelector } from 'react-redux';
import { AiOutlineEdit } from 'react-icons/ai';

import Avatar from '../../../Shared/Avatar';
import { initializeName } from '../../../../util';
import { TRootState, useDeleteTeamCommentMutation } from '../../../../state/store';
import ToolTip from '../../../Shared/ToolTip';
import TeamModal from '../../TeamModal';
import TeamCommentForm from './TeamCommentForm';
import { ITeamComment } from '../../../../interfaces';

export interface ITeamCommentListItemProps {
  teamComment: ITeamComment;
  updateTeamComment: (teamCommentId: number, content: string, tag: string) => void;
  handleResetComments: () => void;
}

const TeamCommentListItem = ({ teamComment, updateTeamComment, handleResetComments }: ITeamCommentListItemProps) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const { user, token } = useSelector((store: TRootState) => store.user);
  const [deleteTeamCommentMutation] = useDeleteTeamCommentMutation();

  const handleCloseModal = (): void => {
    setIsModalOpen(false);
  };

  const handleOpenModal = (): void => {
    setIsModalOpen(true);
  };

  const handleDeleteTeamComment = (): void => {
    const payload = { teamCommentId: teamComment.id, teamPostId: teamComment.teamPostId, token };
    deleteTeamCommentMutation(payload)
      .unwrap()
      .then(() => {
        handleResetComments();
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div data-testid="team-comment-list-item" key={teamComment.id} className="border border-gray-800 rounded p-2 my-2">
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
      <div className="my-1 p-2">
        <span className="mx-1 font-bold text-white">{teamComment.tag}</span>
        {teamComment.content}
      </div>
      {teamComment.userId === user.id && (
        <>
          <div className="mt-2 flex justify-end">
            <div onClick={handleOpenModal} className="mx-1 cursor-pointer">
              <ToolTip message="Edit">
                <AiOutlineEdit />
              </ToolTip>
            </div>
            <div onClick={handleDeleteTeamComment} className="mx-1 mr-4 cursor-pointer">
              <ToolTip message="Remove">
                <BsTrash />
              </ToolTip>
            </div>
          </div>

          {isModalOpen && (
            <TeamModal closeModal={handleCloseModal}>
              <TeamCommentForm
                updateTeamComment={updateTeamComment}
                teamCommentId={teamComment.id}
                handleResetComments={() => {}}
                teamPostId={teamComment.teamPostId}
                formType="edit"
                closeModal={handleCloseModal}
              />
            </TeamModal>
          )}
        </>
      )}
    </div>
  );
};

export default TeamCommentListItem;
