import dayjs from 'dayjs';
import { IMinComment, IReplyComment, ISaveComment } from '../../interfaces';
import Avatar from '../Shared/Avatar';
import { initializeName } from '../../util';

export interface ICommentHeaderProps {
  comment: IMinComment | IReplyComment | ISaveComment;
}

const CommentHeader = ({ comment }: ICommentHeaderProps) => {
  const [first, last] = comment.fullName.split(' ');
  return (
    <div className="flex items-center">
      <Avatar height="h-9" width="w-9" avatarUrl={comment.avatarUrl} initials={initializeName(first, last)} />
      <div className="ml-2">
        <p>{comment.fullName}</p>
        <p className="text-xs">{dayjs(comment.createdAt).format('MM/D/YYYY')}</p>
      </div>
    </div>
  );
};

export default CommentHeader;
