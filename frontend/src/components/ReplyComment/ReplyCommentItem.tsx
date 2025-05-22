import { useState } from 'react';
import { useSelector } from 'react-redux';

import { IError, IMinComment, IReplyComment } from '../../interfaces';
import { TRootState, useDeleteReplyCommentMutation, useUpdateReplyCommentMutation } from '../../state/store';
import ReplyCommentActions from './ReplyCommentActions';
import CommentHeader from '../Comment/CommentHeader';

type IComment = IMinComment | IReplyComment;

export interface IReplyCommentItemProps {
  comment: IComment;
  updateReplyComment?: (commentId: number, content: string) => void;
  deleteReplyComment?: (commentId: number) => void;
  parentCommentId?: number;
}

const ReplyCommentItem = ({
  comment,
  updateReplyComment = () => {},
  deleteReplyComment = () => {},
  parentCommentId = 0,
}: IReplyCommentItemProps) => {
  const MAX_CONTENT_LENGTH = 400;
  const [error, setError] = useState('');
  const [content, setContent] = useState(comment.content);
  const { user, token } = useSelector((store: TRootState) => store.user);
  const [updateReplyCommentMut] = useUpdateReplyCommentMutation();
  const [deleteReplyCommentMut] = useDeleteReplyCommentMutation();
  const [isEditing, setIsEditing] = useState(false);

  const handleSetIsEditing = (editing: boolean): void => {
    setIsEditing(editing);
  };

  const handleDeleteReplyComment = (): void => {
    const payload = { commentId: parentCommentId, replyCommentId: comment.id, token };
    deleteReplyCommentMut(payload)
      .unwrap()
      .then(() => {
        deleteReplyComment(comment.id);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const handleCancelEdit = (): void => {
    setIsEditing(false);
    setContent('');
  };

  const applyServerErrors = <T extends IError>(errors: T): void => {
    for (let prop in errors) {
      setError(errors[prop]);
    }
  };

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>): void => {
    e.preventDefault();
    setError('');
    if (content.trim().length === 0 || content.length > MAX_CONTENT_LENGTH) {
      setError(`Reply comment must be between 1 and ${MAX_CONTENT_LENGTH} characters`);
      return;
    }
    const payload = { commentId: parentCommentId, content, token, replyCommentId: comment.id };
    updateReplyCommentMut(payload)
      .unwrap()
      .then((res) => {
        const newContent = res.data;
        updateReplyComment(comment.id, newContent);
        setIsEditing(false);
        setContent('');
      })
      .catch((err) => {
        applyServerErrors(err.data);
      });
  };

  return (
    <div className="my-8" data-testid="reply-comment-item">
      <div className="border border-gray-800 p-2 rounded">
        <CommentHeader comment={comment} />
        <div className="my-8">
          {isEditing ? (
            <form onSubmit={handleOnSubmit}>
              {error.length > 0 && (
                <div className="flex justify-center my-2">
                  <p className="text-sm text-red-300">{error}</p>
                </div>
              )}
              <div className="flex-col flex md:w-[80%] w-full">
                <label className="text-sm" htmlFor={`edit${comment.id}`}>
                  Edit reply comment
                </label>
                <textarea
                  className="h-20 resize-none p-2 rounded border-gray-800 border bg-transparent"
                  onChange={(e) => setContent(e.target.value)}
                  value={content}
                  id={`edit${comment.id}`}
                  name={`edit${comment.id}`}
                ></textarea>
              </div>

              <div className="my-2">
                <button className="btn text-sm" type="submit">
                  Update
                </button>
                <button onClick={handleCancelEdit} className="text-sm outline-btn !bg-gray-400 ml-4" type="button">
                  Cancel
                </button>
              </div>
            </form>
          ) : (
            <p>{comment.content}</p>
          )}
          {user.id === comment.userId && (
            <ReplyCommentActions
              handleSetIsEditing={handleSetIsEditing}
              handleDeleteReplyComment={handleDeleteReplyComment}
            />
          )}
        </div>
      </div>
    </div>
  );
};

export default ReplyCommentItem;
