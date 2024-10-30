import dayjs from 'dayjs';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { useState } from 'react';

import { IComment, IPaginationState, IReplyComment } from '../../interfaces';
import Avatar from '../Shared/Avatar';
import { initializeName } from '../../util';
import { TRootState, useCreateVoteMutation, useLazyFetchReplyCommentsQuery } from '../../state/store';
import TopicDetailsCommentItemVote from './TopicDetailsCommentItemVote';
import TopicDetailsCommentItemActions from './TopicDetailsCommentItemActions';
import TopicDetailsCommentItemForm from './TopicDetailsCommentItemForm';
import TopicDetailsCommentItemReactions from './TopicDetailsCommentItemReactions';
import ReplyCommentList from '../ReplyComment/ReplyCommentList';

export interface ITopicDetailsCommentItemProps {
  comment: IComment;
  updateCommentVote: (commentId: number, voteType: string) => void;
  updateSavedComment: (commentId: number, curUserHasSaved: boolean) => void;
  updateCommentReaction: (emoji: string, commentId: number) => void;
  removeCommentReaction: (emoji: string, commentId: number) => void;
}

const TopicDetailsCommentItem = ({
  comment,
  updateCommentVote,
  updateSavedComment,
  updateCommentReaction,
  removeCommentReaction,
}: ITopicDetailsCommentItemProps) => {
  const navigate = useNavigate();
  const pagState = {
    page: -1,
    pageSize: 10,
    totalPages: 0,
    direction: 'next',
    totalElements: 0,
  };

  const [fetchReplyComments] = useLazyFetchReplyCommentsQuery();
  const { user, token } = useSelector((store: TRootState) => store.user);
  const [createVoteMut] = useCreateVoteMutation();
  const [isEditing, setIsEditing] = useState(false);
  const [pag, setPag] = useState<IPaginationState>(pagState);
  const [replyComments, setReplyComments] = useState<IReplyComment[]>([]);

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

  const paginateReplyComments = (dir: string): void => {
    if (user.id === 0) {
      navigate('/signin');
      return;
    }

    const payload = {
      token,
      commentId: comment.id,
      direction: dir,
      page: pag.page,
      pageSize: pag.pageSize,
    };
    fetchReplyComments(payload)
      .unwrap()
      .then((res) => {
        const { items, totalPages, totalElements, pageSize, page, direction } = res.data;

        setPag((prevState) => ({
          ...prevState,
          page,
          pageSize,
          totalPages,
          direction,
          totalElements,
        }));

        setReplyComments((prevState) => [...prevState, ...items]);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const updateReplyComment = (commentId: number, content: string): void => {
    setReplyComments(
      replyComments.map((rc) => {
        if (rc.id === commentId) {
          return { ...rc, content };
        }
        return { ...rc };
      })
    );
  };

  const deleteReplyComment = (commentId: number): void => {
    setReplyComments((prevState) => prevState.filter((rc) => rc.id !== commentId));
  };

  return (
    <div>
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
          <div className="flex items-center justify-between">
            <div className="my-2">
              <TopicDetailsCommentItemVote
                curUserHasVoted={comment.curUserHasVoted}
                curUserVoteType={comment.curUserVoteType}
                voteDifference={comment.voteDifference}
                createVote={createVote}
              />
            </div>
            <div className="my-2">
              <TopicDetailsCommentItemActions
                currentUserFullName={user.fullName}
                handleSetIsEditing={handleSetIsEditing}
                commentId={comment.id}
                commentUserId={comment.userId}
                content={comment.content}
                curUserHasSaved={comment.curUserHasSaved}
                updateCommentReaction={updateCommentReaction}
                updateSavedComment={updateSavedComment}
                removeCommentReaction={removeCommentReaction}
                commentAuthorFullName={comment.fullName}
              />
            </div>
          </div>
          <div className="my-1">
            <TopicDetailsCommentItemReactions reactions={comment.reactions} />
          </div>
          {comment.replyCommentsCount > 0 && pag.page === -1 && (
            <div className="flex justify-center">
              <button onClick={() => paginateReplyComments('next')} className="text-sm">
                Reply comments ({comment.replyCommentsCount})
              </button>
            </div>
          )}
        </div>
      </div>
      <>
        <div className="my-2 ml-4">
          <ReplyCommentList
            replyComments={replyComments}
            parentCommentId={comment.id}
            updateReplyComment={updateReplyComment}
            deleteReplyComment={deleteReplyComment}
          />
        </div>
        {pag.page > -1 && pag.page < pag.totalPages - 1 && (
          <div className="flex justify-center">
            <button onClick={() => paginateReplyComments('next')} className="text-sm">
              Load more replies
            </button>
          </div>
        )}
      </>
    </div>
  );
};

export default TopicDetailsCommentItem;
