import { useEffect, useState, useRef } from 'react';
import { useSelector } from 'react-redux';
import { over } from 'stompjs';
import SockJS from 'sockjs-client';

import { initializeName } from '../../util';
import Avatar from '../Shared/Avatar';
import { IError } from '../../interfaces';
import { TRootState, useCreateReplyCommentMutation } from '../../state/store';

export interface ITopicDetailsReplyModalContentProps {
  commentUserId: number;
  currentUserAvatarUrl: string;
  commentAuthorFullName: string;
  currentUserFullName: string;
  currentUserId: number;
  commentId: number;
  comment: string;
  closeModal: () => void;
}

let stompClient: any = null;

const TopicDetailsReplyModalContent = ({
  commentUserId,
  currentUserAvatarUrl,
  commentAuthorFullName,
  currentUserFullName,
  currentUserId,
  commentId,
  comment,
  closeModal,
}: ITopicDetailsReplyModalContentProps) => {
  const MAX_CONTENT_LENGTH = 400;
  const shouldRun = useRef(true);
  const { token } = useSelector((store: TRootState) => store.user);
  const [createReplyComment] = useCreateReplyCommentMutation();
  const [firstName, lastName] = currentUserFullName.split(' ');
  const [content, setContent] = useState('');
  const [error, setError] = useState('');

  const connect = () => {
    if (stompClient && stompClient.connected) {
      console.log('WebSocket already connected');
      return;
    }

    let Sock = new SockJS(import.meta.env.VITE_WEBSOCKET_ENDPOINT);
    stompClient = over(Sock);

    stompClient.connect({}, onConnected, onError);
  };

  const onConnected = () => {
    console.log('WebSocket connected');
  };

  const onError = (err: any) => {
    console.error('WebSocket error:', err);
  };

  useEffect(() => {
    if (shouldRun.current) {
      shouldRun.current = false;
      connect();
    }
    return () => {
      if (stompClient && stompClient.connected) {
        stompClient.disconnect(() => {
          console.log('WebSocket disconnected');
        });
      }
    };
  }, []);

  const applyServerErrors = <T extends IError>(errors: T): void => {
    for (let prop in errors) {
      setError(errors[prop]);
    }
  };

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>): void => {
    e.preventDefault();
    setError('');
    if (content.trim().length === 0 || content.length > MAX_CONTENT_LENGTH) {
      setError('Comment must be between 1 and 400 characters');
      return;
    }
    const payload = { userId: currentUserId, content, commentId, token };
    createReplyComment(payload)
      .unwrap()
      .then(() => {
        closeModal();
      })
      .catch((err) => {
        applyServerErrors(err.data);
      });
  };

  return (
    <form onSubmit={handleOnSubmit}>
      <div className="flex flex-col items-center">
        <p>Replying to {commentAuthorFullName}'s comment</p>
        <div className="my-2 p-2 rounded bg-gray-950">
          <p className="text-sm">{comment}</p>
        </div>
        <Avatar
          width="w-9"
          height="h-9"
          initials={initializeName(firstName, lastName)}
          avatarUrl={currentUserAvatarUrl}
        />
      </div>
      {error.length > 0 && (
        <div className="my-4">
          <p className="text-sm text-red-400">{error}</p>
        </div>
      )}
      <div className="flex flex-col">
        <label htmlFor="content">Reply comment</label>
        <textarea
          value={content}
          onChange={(e) => setContent(e.target.value)}
          id="content"
          name="content"
          className="bg-transparent border border-gray-800 p-2 rounded h-24 resize-none"
        ></textarea>
      </div>
      <div className="my-4">
        <button type="submit" className="btn">
          Reply
        </button>
        <button
          onClick={(e) => {
            e.stopPropagation();
            closeModal();
          }}
          type="button"
          className="outline-btn !bg-gray-400 ml-4"
        >
          Cancel
        </button>
      </div>
    </form>
  );
};

export default TopicDetailsReplyModalContent;
