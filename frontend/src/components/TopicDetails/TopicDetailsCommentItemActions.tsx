import { FaBookmark, FaReply, FaTrash } from 'react-icons/fa';
import { MdEdit } from 'react-icons/md';
import { MdEmojiEmotions } from 'react-icons/md';
import { useSelector } from 'react-redux';

import ToolTip from '../Shared/ToolTip';
import { TRootState, useCreateSaveCommentMutation, useDeleteCommentMutation } from '../../state/store';
import { IoMdFlag } from 'react-icons/io';
import { useState } from 'react';
import TopicDetailsReportCommentModalContent from './TopicDetailsReportCommentModalContent';
import ClickAway from '../Shared/ClickAway';
import TopicDetailsCommentReaction from './TopicDetailsCommentReaction';
import TopicDetailsModal from './TopicDetailsModal';
import TopicDetailsReplyModalContent from './TopicDetailsReplyModalContent';

export interface ITopicDetailsCommentItemActionsProps {
  currentUserFullName: string;
  commentAuthorFullName: string;
  handleSetIsEditing: (editing: boolean) => void;
  commentId: number;
  commentUserId: number;
  content: string;
  curUserHasSaved: boolean;
  updateSavedComment: (commentId: number, curUserHasSaved: boolean) => void;
  updateCommentReaction: (emoji: string, commentId: number) => void;
  removeCommentReaction: (emoji: string, commentId: number) => void;
}

const TopicDetailsCommentItemActions = ({
  currentUserFullName,
  commentAuthorFullName,
  handleSetIsEditing,
  commentId,
  commentUserId,
  content,
  curUserHasSaved,
  updateSavedComment,
  updateCommentReaction,
  removeCommentReaction,
}: ITopicDetailsCommentItemActionsProps) => {
  const { user, token } = useSelector((store: TRootState) => store.user);
  const [deleteComment] = useDeleteCommentMutation();
  const [saveComment] = useCreateSaveCommentMutation();
  const [openModal, setOpenModal] = useState<string | null>(null);
  const [isClickAwayOpen, setIsClickAwayOpen] = useState(false);

  const handleDeleteComment = (): void => {
    const payload = { token, commentId };

    deleteComment(payload)
      .unwrap()
      .then(() => {})
      .catch((err) => {
        console.log(err);
      });
  };

  const handleCloseClickAway = (): void => {
    setIsClickAwayOpen(false);
  };

  const openReplyModal = (e: React.MouseEvent<HTMLDivElement>): void => {
    e.stopPropagation();
    setOpenModal('reply');
  };

  const openReportModal = (e: React.MouseEvent<HTMLDivElement>): void => {
    e.stopPropagation();
    setOpenModal('report');
  };

  const closeModal = (): void => {
    setOpenModal(null);
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
    <div className="flex items-center justify-end ml-2 relative">
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
        <div className="mx-1 cursor-pointer">
          <ToolTip message="React">
            <MdEmojiEmotions onClick={() => setIsClickAwayOpen(true)} />
          </ToolTip>
          {isClickAwayOpen && (
            <ClickAway onClickAway={handleCloseClickAway}>
              <div className="absolute top-5 right-0 bg-gray-800 p-1 rounded z-10 min-h-[60px]">
                <TopicDetailsCommentReaction
                  userId={user.id}
                  commentId={commentId}
                  handleCloseClickAway={handleCloseClickAway}
                  updateCommentReaction={updateCommentReaction}
                  removeCommentReaction={removeCommentReaction}
                />
              </div>
            </ClickAway>
          )}
        </div>
      )}
      {user.id !== 0 && (
        <div onClick={openReplyModal} className="mx-1 cursor-pointer">
          <ToolTip message="Reply">
            <FaReply />
          </ToolTip>
          {openModal && (
            <TopicDetailsModal closeModal={closeModal}>
              <TopicDetailsReplyModalContent
                currentUserFullName={currentUserFullName}
                currentUserAvatarUrl={user.avatarUrl}
                commentAuthorFullName={commentAuthorFullName}
                currentUserId={user.id}
                commentId={commentId}
                comment={content}
                closeModal={closeModal}
              />
            </TopicDetailsModal>
          )}
        </div>
      )}
      {user.id !== 0 && (
        <div onClick={openReportModal} className="mx-1 cursor-pointer">
          <ToolTip message="Report">
            <IoMdFlag />
          </ToolTip>
          {openModal === 'report' && (
            <TopicDetailsModal closeModal={closeModal}>
              <TopicDetailsReportCommentModalContent
                currentUserId={user.id}
                commentId={commentId}
                content={content}
                closeModal={closeModal}
              />
            </TopicDetailsModal>
          )}
        </div>
      )}
    </div>
  );
};

export default TopicDetailsCommentItemActions;
