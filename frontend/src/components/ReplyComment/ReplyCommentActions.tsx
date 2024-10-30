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
      <div onClick={() => handleSetIsEditing(true)} className="mx-1 cursor-pointer">
        <ToolTip message="Edit">
          <MdEdit />
        </ToolTip>
      </div>
      <div onClick={handleDeleteReplyComment} className="mx-1 cursor-pointer">
        <ToolTip message="Delete">
          <FaTrash />
        </ToolTip>
      </div>
    </div>
  );
};

export default ReplyCommentActions;
