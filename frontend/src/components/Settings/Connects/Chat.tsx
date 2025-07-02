import { useDispatch, useSelector } from 'react-redux';
import { useEffect, useRef, useState } from 'react';
import dayjs from 'dayjs';
import { TRootState, addMessage, setMessages, useLazyFetchChatMessagesQuery } from '../../../state/store';
import { IMessage } from '../../../interfaces';
import Avatar from '../../Shared/Avatar';
import Spinner from '../../Shared/Spinner';
import { connectWebSocket, disconnectWebSocket, sendMessage, subscribeToTopic } from '../../../util/WebSocketService';

const Chat = () => {
  const dispatch = useDispatch();
  const { user, token } = useSelector((store: TRootState) => store.user);
  const { messages, currentConnection } = useSelector((store: TRootState) => store.chat);
  const [fetchChatMessages, { isLoading }] = useLazyFetchChatMessagesQuery();
  const currentConnectionRef = useRef(currentConnection);
  const [message, setMessage] = useState<string>('');
  const messagesEndRef = useRef<HTMLDivElement>(null);
  const subscriptionRef = useRef<any>(null);

  const onConnected = () => {
    if (subscriptionRef.current) {
      subscriptionRef.current.unsubscribe();
    }
    subscriptionRef.current = subscribeToTopic(`/user/${user.id}/topic/messages`, onFetchMessages);
  };

  const onFetchMessages = (data: any) => {
    const newMessage = JSON.parse(data.body) as IMessage;
    const { senderId, receiverId } = currentConnectionRef.current;

    if (newMessage.userId === senderId || newMessage.userId === receiverId) {
      dispatch(addMessage(newMessage));
    }
  };

  const emitMessage = () => {
    const payload = {
      connectionId: currentConnection.id,
      userId: user.id,
      text: message,
    };
    sendMessage('/api/v1/messages', JSON.stringify(payload));
  };

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  const handleSendMessage = () => {
    if (message.trim().length > 0) {
      emitMessage();
      setMessage('');
    }
  };

  const onError = (error: any) => {
    console.error('WebSocket error:', error);
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages.length]);

  useEffect(() => {
    currentConnectionRef.current = currentConnection;

    if (user.id !== 0 && currentConnection.id !== 0) {
      connectWebSocket(onConnected, onError);
      fetchChatMessages({ token, connectionId: currentConnection.id })
        .unwrap()
        .then((res) => dispatch(setMessages(res.data)))
        .catch((err) => console.log(err));
    }

    return () => {
      if (subscriptionRef.current) {
        subscriptionRef.current.unsubscribe();
      }
      disconnectWebSocket();
    };
  }, [user.id, currentConnection.id]);

  return (
    <div className="flex flex-col justify-between min-h-[90vh] p-2">
      <div>
        <h3 className="text-xl">
          {currentConnection.firstName} {currentConnection.lastName}
        </h3>
        {isLoading && <Spinner message="Loading chat..." />}
        <div className="overflow-y-auto h-[600px]">
          <div className="my-4 p-2 flex flex-col-reverse">
            {messages.map((message) => (
              <div
                data-testid="settings-chat-message-item"
                key={message.id}
                className={`flex flex-col ${message.userId === user.id ? 'items-end' : 'items-start'}`}
              >
                <div className="flex items-center">
                  <Avatar width="w-9" height="h-9" initials="?.?" avatarUrl={message.avatarUrl} />
                  <p className="text-gray-400 ml-2">
                    {message.firstName} {message.lastName}
                  </p>
                </div>
                <div className="my-2 bg-green-400 rounded-lg p-2">
                  <p className="text-black">{message.text}</p>
                  <div className="flex justify-end">
                    <p className="text-xs">{dayjs(message.createdAt).format('MM/DD/YYYY')}</p>
                  </div>
                </div>
              </div>
            ))}
          </div>
          <div ref={messagesEndRef}></div>
        </div>
      </div>
      <div className="flex items-center">
        <input
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          className="w-[80%] border rounded border-gray-800 bg-transparent h-9"
        />
        <button onClick={handleSendMessage} className="w-[20%] border rounded border-gray-800 h-9 ml-1">
          Send
        </button>
      </div>
    </div>
  );
};

export default Chat;
