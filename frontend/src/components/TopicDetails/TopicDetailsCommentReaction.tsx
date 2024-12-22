import { nanoid } from 'nanoid';
import { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';

import {
  TRootState,
  useCreateReactionMutation,
  useDeleteReactionMutation,
  useFetchReactionQuery,
} from '../../state/store';

export interface ITopicDetailsCommentReactionProps {
  userId: number;
  commentId: number;
  updateCommentReaction: (emoji: string, commentId: number) => void;
  removeCommentReaction: (emoji: string, commentId: number) => void;
}

const TopicDetailsCommentReaction = ({
  userId,
  commentId,
  updateCommentReaction,
  removeCommentReaction,
}: ITopicDetailsCommentReactionProps) => {
  const emojis = ['👍', '❤️', '😂', '😮', '😢', '😡'];
  const { token } = useSelector((store: TRootState) => store.user);
  const [createReaction] = useCreateReactionMutation();
  const [deleteReaction] = useDeleteReactionMutation();
  const { data } = useFetchReactionQuery({ token, commentId, userId }, { skip: !token || !commentId || !userId });
  const [selectedEmoji, setSelectedEmoji] = useState<string | null>(null);

  useEffect(() => {
    if (data !== undefined) {
      setSelectedEmoji(data.data);
    }
  }, [data]);

  const handleOnClick = (e: React.MouseEvent<HTMLDivElement>, emoji: string): void => {
    e.stopPropagation();

    if (selectedEmoji !== null) {
      handleDeleteReaction(selectedEmoji);
    } else {
      handleCreateReaction(emoji);
    }
  };

  const handleDeleteReaction = (emoji: string): void => {
    const payload = { userId, commentId, token };
    deleteReaction(payload)
      .unwrap()
      .then(() => {
        removeCommentReaction(emoji, commentId);
        setSelectedEmoji(null);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const handleCreateReaction = (emoji: string): void => {
    const payload = { userId, commentId, emoji, token };
    createReaction(payload)
      .unwrap()
      .then(() => {
        updateCommentReaction(emoji, commentId);
        setSelectedEmoji(emoji);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div className="flex flex-wrap">
      {emojis.map((emoji) => {
        return (
          <div
            onClick={(e) => handleOnClick(e, emoji)}
            key={nanoid()}
            className={`mx-1 cursor-pointer ${emoji === selectedEmoji ? 'bg-blue-400 rounded' : ''}`}
          >
            {emoji}
          </div>
        );
      })}
    </div>
  );
};

export default TopicDetailsCommentReaction;
