import { IReplyComment } from '../../interfaces';
import ReplyCommentItem from './ReplyCommentItem';

export interface IReplyCommentListProps {
  replyComments: IReplyComment[];
  updateReplyComment?: (commentId: number, content: string) => void;
  deleteReplyComment?: (commentId: number) => void;
  parentCommentId?: number;
}

const ReplyCommentList = ({
  replyComments,
  updateReplyComment = () => {},
  deleteReplyComment = () => {},
  parentCommentId = 0,
}: IReplyCommentListProps) => {
  return (
    <div className="my-4">
      {replyComments.map((replyComment) => {
        return (
          <ReplyCommentItem
            updateReplyComment={updateReplyComment}
            deleteReplyComment={deleteReplyComment}
            key={replyComment.id}
            comment={replyComment}
            parentCommentId={parentCommentId}
          />
        );
      })}
    </div>
  );
};

export default ReplyCommentList;
