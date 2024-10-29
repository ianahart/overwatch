import { useState } from 'react';
import { AiOutlineClose } from 'react-icons/ai';
import { useSelector } from 'react-redux';

import { TRootState, useCreateCommentMutation } from '../../state/store';
import { IError } from '../../interfaces';
import Avatar from '../Shared/Avatar';
import { initializeName } from '../../util';

export interface ITopicDetailsCommentModalContentProps {
  topicId: number;
  handleCloseModal: () => void;
}

const TopicDetailsCommentModalContent = ({ topicId, handleCloseModal }: ITopicDetailsCommentModalContentProps) => {
  const MAX_CHARS = 400;
  const { user, token } = useSelector((store: TRootState) => store.user);
  const [createCommentMut] = useCreateCommentMutation();
  const [content, setContent] = useState('');
  const [error, setError] = useState('');

  const applyServerErrors = <T extends IError>(errors: T) => {
    for (let prop in errors) {
      setError(errors[prop]);
    }
  };

  const createComment = () => {
    const payload = { userId: user.id, topicId, token, content };

    createCommentMut(payload)
      .unwrap()
      .then(() => {
        setContent('');
        handleCloseModal();
      })
      .catch((err) => {
        applyServerErrors(err.data);
      });
  };

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError('');
    if (content.trim().length === 0 || content.length > MAX_CHARS) {
      setError('Comment must not be empty and must be under 400 characters');
      return;
    }
    createComment();
  };

  return (
    <div className="my-8 relative">
      <div className="fixed inset-0 bg-gray-800 bg-opacity-90">
        <div className="m-2 flex justify-end">
          <div onClick={handleCloseModal} className="bg-black p-2 rounded-3xl cursor-pointer">
            <AiOutlineClose className="text-4xl text-white" />
          </div>
        </div>
        <div className="flex items-center flex-col justify-center min-h-[60vh]">
          <div className="max-w-[600px] bg-gray-900 rounded shadow-lg p-2 w-full">
            <form onSubmit={handleOnSubmit}>
              <div className="flex items-center flex-col">
                <h3 className="text-2xl">Add a comment</h3>
                <p>When posting a comment please be respectful to other users.</p>
                <div className="my-2 flex flex-col items-center">
                  <Avatar
                    initials={initializeName(user.firstName, user.lastName)}
                    avatarUrl={user.avatarUrl}
                    height="h-9"
                    width="w-9"
                  />
                  <p>
                    Posting a comment as{' '}
                    <span className="font-bold text-gray-400">
                      {user.firstName} {user.lastName}
                    </span>
                  </p>
                </div>
              </div>
              <div className="flex flex-col mt-8">
                <label htmlFor="comment">Comment</label>
                <textarea
                  placeholder="Write your comment here"
                  onChange={(e) => setContent(e.target.value)}
                  value={content}
                  className="resize-none md:w-[80%] h-24 w-full bg-transparent border rounded border-gray-800"
                  id="comment"
                  name="comment"
                ></textarea>
              </div>
              {error.length > 0 && (
                <div className="flex justify-center">
                  <p className="text-red-300 text-sm">{error}</p>
                </div>
              )}
              <div className="my-8">
                <button type="submit" className="btn mr-2">
                  Add comment
                </button>
                <button
                  onClick={handleCloseModal}
                  type="button"
                  className="btn-outline bg-gray-400 !text-black rounded p-2 h-9 ml-2"
                >
                  Cancel
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default TopicDetailsCommentModalContent;
