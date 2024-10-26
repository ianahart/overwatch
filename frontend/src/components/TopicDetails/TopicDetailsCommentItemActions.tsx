import { FaTrash } from 'react-icons/fa';
import { MdEdit } from 'react-icons/md';
import { useSelector } from 'react-redux';

import ToolTip from '../Shared/ToolTip';
import { TRootState, useDeleteCommentMutation } from '../../state/store';
import { IoMdFlag } from 'react-icons/io';
import { useState } from 'react';
import TopicDetailsReportCommentModal from './TopicDetailsReportCommentModal';

export interface ITopicDetailsCommentItemActionsProps {
  handleSetIsEditing: (editing: boolean) => void;
  commentId: number;
  commentUserId: number;
  content: string;
}

const TopicDetailsCommentItemActions = ({
  handleSetIsEditing,
  commentId,
  commentUserId,
  content,
}: ITopicDetailsCommentItemActionsProps) => {
  const { user, token } = useSelector((store: TRootState) => store.user);
  const [deleteComment] = useDeleteCommentMutation();
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleDeleteComment = () => {
    const payload = { token, commentId };

    deleteComment(payload)
      .unwrap()
      .then(() => {})
      .catch((err) => {
        console.log(err);
      });
  };

  const openReportCommentModal = (e: React.MouseEvent<HTMLDivElement>) => {
    e.stopPropagation();
    setIsModalOpen(true);
  };

  const closeReportCommentModal = () => {
    setIsModalOpen(false);
  };

  return (
    <div className="flex items-center ml-2">
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
