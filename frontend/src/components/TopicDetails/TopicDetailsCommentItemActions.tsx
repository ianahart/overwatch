import { FaTrash } from 'react-icons/fa';
import { MdEdit } from 'react-icons/md';
import ToolTip from '../Shared/ToolTip';

export interface ITopicDetailsCommentItemActionsProps {
  handleSetIsEditing: (editing: boolean) => void;
  commentId: number;
}

const TopicDetailsCommentItemActions = ({ handleSetIsEditing, commentId }: ITopicDetailsCommentItemActionsProps) => {
  return (
    <div className="flex items-center ml-2">
      <div onClick={() => handleSetIsEditing(true)} className="mx-1 cursor-pointer">
        <ToolTip message="Edit">
          <MdEdit />
        </ToolTip>
      </div>
      <div className="mx-1 cursor-pointer">
        <ToolTip message="Delete">
          <FaTrash />
        </ToolTip>
      </div>
    </div>
  );
};

export default TopicDetailsCommentItemActions;
