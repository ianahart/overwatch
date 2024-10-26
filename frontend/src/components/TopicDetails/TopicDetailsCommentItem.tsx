import dayjs from 'dayjs';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import { IComment } from '../../interfaces';
import Avatar from '../Shared/Avatar';
import { initializeName } from '../../util';
import { TRootState, useCreateVoteMutation } from '../../state/store';
import TopicDetailsCommentItemVote from './TopicDetailsCommentItemVote';
import TopicDetailsCommentItemActions from './TopicDetailsCommentItemActions';
import { useState } from 'react';
import TopicDetailsCommentItemForm from './TopicDetailsCommentItemForm';

export interface ITopicDetailsCommentItemProps {
  comment: IComment;
  updateCommentVote: (commentId: number, voteType: string) => void;
}

const TopicDetailsCommentItem = ({ comment, updateCommentVote }: ITopicDetailsCommentItemProps) => {
  const navigate = useNavigate();
  const { user, token } = useSelector((store: TRootState) => store.user);
  const [createVoteMut] = useCreateVoteMutation();
  const [isEditing, setIsEditing] = useState(false);

  const [firstName, lastName] = comment.fullName.split(' ');

  const createVote = (voteType: string) => {
    if (user.id === 0) {
      navigate('/signin');
      return;
    }

    const payload = { userId: user.id, commentId: comment.id, token, voteType };
    createVoteMut(payload)
      .unwrap()
      .then(() => {
        updateCommentVote(comment.id, voteType);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const handleSetIsEditing = (editing: boolean): void => {
    setIsEditing(editing);
  };

  return (
    <div className="border my-4 p-2 rounded-lg border-gray-800">
      <div className="my-2">
        <div className="flex items-center">
          <Avatar
            height="h-9"
            width="w-9"
            avatarUrl={comment.avatarUrl}
            initials={initializeName(firstName, lastName)}
          />
          <div className="ml-2">
            <h3>{comment.fullName}</h3>
            {comment.isEdited && <p className="text-xs">(edited)</p>}
            <p className="text-xs">{dayjs(comment.createdAt).format('MM/DD/YYYY')}</p>
          </div>
        </div>
        <div className="my-2">
          {isEditing ? (
            <TopicDetailsCommentItemForm
              handleSetIsEditing={handleSetIsEditing}
              commentId={comment.id}
              content={comment.content}
            />
          ) : (
            <p>{comment.content}</p>
          )}
        </div>
        <div className="my-2">
          <TopicDetailsCommentItemVote
            curUserHasVoted={comment.curUserHasVoted}
            curUserVoteType={comment.curUserVoteType}
            voteDifference={comment.voteDifference}
            createVote={createVote}
          />
        </div>
        <div className="my-2">
          {user.id === comment.userId && (
            <TopicDetailsCommentItemActions handleSetIsEditing={handleSetIsEditing} commentId={comment.id} />
          )}
        </div>
      </div>
    </div>
  );
};

export default TopicDetailsCommentItem;
