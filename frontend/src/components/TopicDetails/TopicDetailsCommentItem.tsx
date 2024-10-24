import dayjs from 'dayjs';
import { IComment } from '../../interfaces';
import Avatar from '../Shared/Avatar';
import { initializeName } from '../../util';

export interface ITopicDetailsCommentItemProps {
  comment: IComment;
}

const TopicDetailsCommentItem = ({ comment }: ITopicDetailsCommentItemProps) => {
  const [firstName, lastName] = comment.fullName.split(' ');

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
            <p className="text-xs">{dayjs(comment.createdAt).format('MM/DD/YYYY')}</p>
          </div>
        </div>
        <div className="my-2">
          <p>{comment.content}</p>
        </div>
      </div>
    </div>
  );
};

export default TopicDetailsCommentItem;
