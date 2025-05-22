import { MdEdit } from 'react-icons/md';
import ToolTip from '../Shared/ToolTip';
import { FaTrash } from 'react-icons/fa';

export interface IReplyCommentActionsProps {
  handleSetIsEditing: (editing: boolean) => void;
  handleDeleteReplyComment: () => void;
}

const ReplyCommentActions = ({ handleSetIsEditing, handleDeleteReplyComment }: IReplyCommentActionsProps) => {
  return (
    <div className="flex items-center justify-end ml-2 relative">
      <div data-testid="reply-comment-edit" onClick={() => handleSetIsEditing(true)} className="mx-1 cursor-pointer">
        <ToolTip message="Edit">
          <MdEdit role="edit-reply-comment-icon" />
        </ToolTip>
      </div>
      <div data-testid="reply-comment-delete" onClick={handleDeleteReplyComment} className="mx-1 cursor-pointer">
        <ToolTip message="Delete">
          <FaTrash role="delete-reply-comment-icon" />
        </ToolTip>
      </div>
    </div>
  );
};

export default ReplyCommentActions;
