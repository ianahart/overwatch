import { IMinComment } from '../../interfaces';
import CommentItem from './CommentItem';

export interface IOriginalCommentProps {
  comment: IMinComment;
}

const OriginalComment = ({ comment }: IOriginalCommentProps) => {
  return (
    <div className="my-2">
      <h3 className="text-gray-400 text-xl">Original Comment</h3>
      <CommentItem comment={comment} />
    </div>
  );
};
export default OriginalComment;
