import { FaTrash } from 'react-icons/fa';
import { MdEdit } from 'react-icons/md';
import { useSelector } from 'react-redux';

import ToolTip from '../Shared/ToolTip';
import { TRootState, useDeleteCommentMutation } from '../../state/store';

export interface ITopicDetailsCommentItemActionsProps {
  handleSetIsEditing: (editing: boolean) => void;
  commentId: number;
}

const TopicDetailsCommentItemActions = ({ handleSetIsEditing, commentId }: ITopicDetailsCommentItemActionsProps) => {
  const { token } = useSelector((store: TRootState) => store.user);
  const [deleteComment] = useDeleteCommentMutation();

  const handleDeleteComment = () => {
    const payload = { token, commentId };

    deleteComment(payload)
      .unwrap()
      .then(() => {})
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div className="flex items-center ml-2">
      <div onClick={() => handleSetIsEditing(true)} className="mx-1 cursor-pointer">
        <ToolTip message="Edit">
          <MdEdit />
        </ToolTip>
      </div>
      <div onClick={handleDeleteComment} className="mx-1 cursor-pointer">
        <ToolTip message="Delete">
          <FaTrash />
        </ToolTip>
      </div>
    </div>
  );
};

export default TopicDetailsCommentItemActions;
