import { FaBookmark, FaTrash } from 'react-icons/fa';
import { MdEdit } from 'react-icons/md';
import { useSelector } from 'react-redux';

import ToolTip from '../Shared/ToolTip';
import { TRootState, useCreateSaveCommentMutation, useDeleteCommentMutation } from '../../state/store';
import { IoMdFlag } from 'react-icons/io';
import { useState } from 'react';
import TopicDetailsReportCommentModal from './TopicDetailsReportCommentModal';

export interface ITopicDetailsCommentItemActionsProps {
  handleSetIsEditing: (editing: boolean) => void;
  commentId: number;
  commentUserId: number;
  content: string;
  curUserHasSaved: boolean;
  updateSavedComment: (commentId: number, curUserHasSaved: boolean) => void;
}

const TopicDetailsCommentItemActions = ({
  handleSetIsEditing,
  commentId,
  commentUserId,
  content,
  curUserHasSaved,
  updateSavedComment,
}: ITopicDetailsCommentItemActionsProps) => {
  const { user, token } = useSelector((store: TRootState) => store.user);
  const [deleteComment] = useDeleteCommentMutation();
  const [saveComment] = useCreateSaveCommentMutation();
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleDeleteComment = (): void => {
    const payload = { token, commentId };

    deleteComment(payload)
      .unwrap()
      .then(() => {})
      .catch((err) => {
        console.log(err);
      });
  };

  const openReportCommentModal = (e: React.MouseEvent<HTMLDivElement>): void => {
    e.stopPropagation();
    setIsModalOpen(true);
  };

  const closeReportCommentModal = (): void => {
    setIsModalOpen(false);
  };

  const handleOnSaveComment = (): void => {
    const payload = { userId: user.id, commentId, token };

    saveComment(payload)
      .unwrap()
      .then(() => {
        updateSavedComment(commentId, true);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div className="flex items-center justify-end ml-2">
      {user.id === commentUserId && (
        <div onClick={() => handleSetIsEditing(true)} className="mx-1 cursor-pointer">
          <ToolTip message="Edit">
            <MdEdit />
          </ToolTip>
        </div>
      )}
      {user.id === commentUserId && (
        <div onClick={handleDeleteComment} className="mx-1 cursor-pointer">
          <ToolTip message="Delete">
            <FaTrash />
          </ToolTip>
        </div>
      )}
      {user.id !== 0 && (
        <div onClick={handleOnSaveComment} className="mx-1 cursor-pointer">
          <ToolTip message={`${curUserHasSaved ? 'Saved' : 'Save'}`}>
            <FaBookmark className={`${curUserHasSaved ? 'text-yellow-400' : 'text-gray-400'}`} />
          </ToolTip>
        </div>
      )}
      {user.id !== 0 && (
        <div onClick={openReportCommentModal} className="mx-1 cursor-pointer">
          <ToolTip message="Report">
            <IoMdFlag />
          </ToolTip>
          {isModalOpen && (
            <TopicDetailsReportCommentModal
              currentUserId={user.id}
              commentId={commentId}
              content={content}
              closeReportCommentModal={closeReportCommentModal}
            />
          )}
        </div>
      )}
    </div>
  );
};

export default TopicDetailsCommentItemActions;
