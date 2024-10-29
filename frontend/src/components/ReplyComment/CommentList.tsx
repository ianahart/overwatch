import { IReplyComment } from '../../interfaces';
import CommentItem from './CommentItem';

export interface ICommentListProps {
  replyComments: IReplyComment[];
}

const CommentList = ({ replyComments }: ICommentListProps) => {
  return (
    <div className="my-4">
      {replyComments.map((replyComment) => {
        return <CommentItem key={replyComment.id} comment={replyComment} />;
      })}
    </div>
  );
};

export default CommentList;
