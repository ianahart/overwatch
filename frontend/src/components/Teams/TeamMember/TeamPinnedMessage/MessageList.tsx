import { useSelector } from 'react-redux';
import { useEffect, useState } from 'react';

import { ITeam, ITeamPinnedMessage } from '../../../../interfaces';
import { TRootState, useFetchTeamPinnedMessagesQuery } from '../../../../state/store';
import MessageListItem from './MessageListItem';

export interface IMessageListProps {
  team: ITeam;
}

const MessageList = ({ team }: IMessageListProps) => {
  const { token, user } = useSelector((store: TRootState) => store.user);
  const [messages, setMessages] = useState<ITeamPinnedMessage[]>([]);
  const { data } = useFetchTeamPinnedMessagesQuery({ token, teamId: team.id }, { skip: !token || !team.id });

  useEffect(() => {
    if (data !== undefined) {
      setMessages(data.data);
    }
  }, [data]);

  return (
    <div>
      <div>
        {messages.map((message) => {
          return <MessageListItem team={team} user={user} key={message.id} message={message} />;
        })}
      </div>
    </div>
  );
};

export default MessageList;
