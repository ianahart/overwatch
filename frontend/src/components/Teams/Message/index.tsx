import { useEffect, useRef, useState } from 'react';
import { connectWebSocket, sendMessage, disconnectWebSocket, subscribeToTopic } from '../../../util/WebSocketService';
import { useDispatch, useSelector } from 'react-redux';
import dayjs from 'dayjs';

import { TRootState, addTeamMessage, setTeamMessages, useLazyFetchTeamMessagesQuery } from '../../../state/store';
import Avatar from '../../Shared/Avatar';
import { ITeamMessage } from '../../../interfaces';

const Message = () => {
  const dispatch = useDispatch();
  const { currentTeam, teamMessages } = useSelector((store: TRootState) => store.team);
  const { user, token } = useSelector((store: TRootState) => store.user);
  const [fetchTeamMessages] = useLazyFetchTeamMessagesQuery();
  const [inputMessage, setInputMessage] = useState('');
  const subscriptionRef = useRef<any>(null);
  const messagesEndRef = useRef<HTMLDivElement>(null);

  const onConnected = () => {
    if (subscriptionRef.current) {
      subscriptionRef.current.unsubscribe();
    }
    subscriptionRef.current = subscribeToTopic(`/user/${user.id}/topic/team-messages`, onFetchMessages);
  };

  const handleSendMessage = () => {
    if (inputMessage.trim().length > 0) {
      emitMessage();
      setInputMessage('');
    }
  };

  const onError = (error: any) => {
    console.error('WebSocket error:', error);
  };
  const onFetchMessages = (data: any) => {
    const newMessage = JSON.parse(data.body) as ITeamMessage;
    console.log(newMessage);
    if (newMessage.teamId === currentTeam) {
      dispatch(addTeamMessage(newMessage));
    }
  };

  const emitMessage = () => {
    const payload = {
      teamId: currentTeam,
      userId: user.id,
      text: inputMessage,
    };
    sendMessage('/api/v1/team-messages', JSON.stringify(payload));
  };
  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(() => {
    scrollToBottom();
  }, [teamMessages.length]);

  useEffect(() => {
    if (user.id !== 0 && currentTeam !== 0) {
      connectWebSocket(onConnected, onError);
      fetchTeamMessages({ token, currentTeam })
        .unwrap()
        .then((res) => dispatch(setTeamMessages(res.data)))
        .catch((err) => console.log(err));
    }

    return () => {
      if (subscriptionRef.current) {
        subscriptionRef.current.unsubscribe();
      }
      disconnectWebSocket();
    };
  }, [user.id, currentTeam]);

  return (
    <div className="max-w-[700px] w-full">
      <div className="border rounded border-gray-800 w-full p-2 h-[450px] overflow-y-auto">
        <div className="my-4 p-2 flex flex-col-reverse">
          {teamMessages.map((teamMessage) => (
            <div
              key={teamMessage.id}
              className={`flex flex-col ${teamMessage.userId === user.id ? 'items-end' : 'items-start'}`}
            >
              <div className="flex items-center">
                <Avatar width="w-9" height="h-9" initials="?.?" avatarUrl={teamMessage.avatarUrl} />
                <p className="text-gray-400 ml-2">{teamMessage.fullName}</p>
              </div>
              <div className="my-2 bg-green-400 rounded-lg p-2">
                <p className="text-black">{teamMessage.text}</p>
                <div className="flex justify-end">
                  <p className="text-xs">{dayjs(teamMessage.createdAt).format('MM/DD/YYYY')}</p>
                </div>
              </div>
            </div>
          ))}
        </div>
        <div ref={messagesEndRef}></div>
      </div>
      <div className="my-4 w-full flex">
        <input
          value={inputMessage}
          onChange={(e) => setInputMessage(e.target.value)}
          placeholder="Start typing..."
          className=" w-[85%] p-1 border border-gray-800 rounded h-9 bg-transparent"
        />
        <button onClick={handleSendMessage} className="btn !bg-blue-400 w-[125px]">
          Send
        </button>
      </div>
    </div>
  );
};

export default Message;
