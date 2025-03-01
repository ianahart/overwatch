import dayjs from 'dayjs';
import { ITeamPinnedMessage, IUser } from '../../../../interfaces';
import { initializeName } from '../../../../util';
import Avatar from '../../../Shared/Avatar';

export interface IMessageListItemProps {
  message: ITeamPinnedMessage;
  user: IUser;
}

const MessageListItem = ({ message, user }: IMessageListItemProps) => {
  const [first, last] = message.fullName.split(' ');
  console.log(message);
  return (
    <div className="my-4 border border-gray-800 rounded-md p-2">
      <div className="flex items-center">
        <Avatar avatarUrl={message.avatarUrl} height="h-9" width="w-9" initials={`${initializeName(first, last)}`} />
        <div>
          <h4 className="text-sm ml-2">
            {first} {last}
          </h4>
          <p className="text-xs">{message.isEdited ? '(edited)' : ''}</p>
          <p className="text-xs text-green-400 ml-2">{dayjs(message.createdAt).format('MM/DD/YYYY')}</p>
        </div>
      </div>
      <div>
        <p>{message.message}</p>
      </div>
    </div>
  );
};

export default MessageListItem;
