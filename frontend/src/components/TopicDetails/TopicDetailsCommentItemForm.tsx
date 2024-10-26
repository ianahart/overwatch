import { useState } from 'react';
import { useSelector } from 'react-redux';
import { TRootState, useUpdateCommentMutation } from '../../state/store';
import { IError } from '../../interfaces';

export interface ITopicDetailsCommentItemFormProps {
  handleSetIsEditing: (editing: boolean) => void;
  commentId: number;
  content: string;
}

const TopicDetailsCommentItemForm = ({ handleSetIsEditing, commentId, content }: ITopicDetailsCommentItemFormProps) => {
  const MAX_CONTENT_LENGTH = 400;
  const { token, user } = useSelector((store: TRootState) => store.user);
  const [newContent, setNewContent] = useState(content);
  const [error, setError] = useState('');
  const [updateComment] = useUpdateCommentMutation();
  const attribute = `content${commentId}`;

  const applyServerErrors = <T extends IError>(errors: T): void => {
    for (let prop in errors) {
      setError(errors[prop]);
    }
  };

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError('');

            if (newContent.trim().length === 0 || newContent.length > MAX_CONTENT_LENGTH) {
                setError('Comment must be between 1 and 400 characters');
                return;
            }
    
    const payload = { commentId, userId: user.id, token, content: newContent };

    updateComment(payload)
      .unwrap()
      .then(() => {
        handleSetIsEditing(false);
      })
      .catch((err) => {
        applyServerErrors(err.data);
      });
  };

  return (
    <form onSubmit={handleOnSubmit} className="my-4">
      <div className="flex flex-col">
        <label htmlFor={`${attribute}`} className="text-sm">
          Edit comment
        </label>
        {error.length > 0 && (
          <div className="my-2">
            <p className="text-sm text-red-300">{error}</p>
          </div>
        )}
        <textarea
          value={newContent}
          onChange={(e) => setNewContent(e.target.value)}
          id={`${attribute}`}
          name={`${attribute}`}
          className="md:w-[70%] w-full resize-none bg-transparent border border-gray-800 p-2 rounded"
        ></textarea>
      </div>
      <div className="flex my-2">
        <button type="submit" className="text-sm mx-2 border border-gray-800 p-1 text-gray-400 rounded">
          Update
        </button>
        <button
          type="button"
          onClick={() => handleSetIsEditing(false)}
          className="text-sm mx-2 p-1 border border-gray-800 text-gray-400 rounded"
        >
          Cancel
        </button>
      </div>
    </form>
  );
};

export default TopicDetailsCommentItemForm;
