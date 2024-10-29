import dayjs from 'dayjs';
import { IMinComment, IReplyComment } from '../../interfaces';
import Avatar from '../Shared/Avatar';
import { initializeName } from '../../util';

type IComment = IMinComment | IReplyComment;

export interface ICommentItemProps {
  comment: IComment;
}

const CommentItem = ({ comment }: ICommentItemProps) => {
  const [first, last] = comment.fullName.split(' ');
  return (
    <div className="my-8">
      <div className="border border-gray-800 p-2 rounded">
        <div className="flex items-center">
          <Avatar height="h-9" width="w-9" avatarUrl={comment.avatarUrl} initials={initializeName(first, last)} />
          <div className="ml-2">
            <p>{comment.fullName}</p>
            <p className="text-xs">{dayjs(comment.createdAt).format('MM/D/YYYY')}</p>
          </div>
        </div>
        <div className="my-8">
          <p>{comment.content}</p>
        </div>
      </div>
    </div>
  );
};

export default CommentItem;
