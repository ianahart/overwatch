import { useDispatch, useSelector } from 'react-redux';
import { useEffect, useRef, useState } from 'react';
import { over } from 'stompjs';
import SockJS from 'sockjs-client';
// @ts-ignore
import dayjs from 'dayjs';

let stompClient: any = null;

import { TRootState, addMessage, setMessages, useLazyFetchChatMessagesQuery } from '../../../state/store';
import { IMessage } from '../../../interfaces';
import Avatar from '../../Shared/Avatar';
import Spinner from '../../Shared/Spinner';

const Chat = () => {
  const dispatch = useDispatch();
  const { user, token } = useSelector((store: TRootState) => store.user);
  const { messages } = useSelector((store: TRootState) => store.chat);
  const [fetchChatMessages, { isLoading }] = useLazyFetchChatMessagesQuery();
  const { currentConnection } = useSelector((store: TRootState) => store.chat);
  const currentConnectionRef = useRef(currentConnection);

  useEffect(() => {
    currentConnectionRef.current = currentConnection;
  }, [currentConnection]);

  const [message, setMessage] = useState<string>('');
  const messagesEndRef = useRef<HTMLDivElement>(null);

  const connect = () => {
    if (stompClient && stompClient.connected) {
      console.log('WebSocket already connected');
      return;
    }

    let Sock = new SockJS('http://localhost:8080/ws');
    stompClient = over(Sock);

    stompClient.connect({}, onConnected, onError);
  };

  const onConnected = () => {
    stompClient.subscribe(`/user/${user.id}/topic/messages`, onFetchMessages);
  };

  const onFetchMessages = (data: any) => {
    const newMessage = JSON.parse(data.body) as IMessage;
    console.log(
      `newMessage.userId: ${newMessage.userId} currentConnection.senderId: ${currentConnection.senderId} currentConnection.recevierId ${currentConnection.receiverId}`
    );
    const { senderId, receiverId } = currentConnectionRef.current;

    if (newMessage.userId === senderId || newMessage.userId === receiverId) {
      dispatch(addMessage(newMessage));
    }
  };

  const emitMessage = () => {
    if (stompClient && stompClient.connected) {
      const payload = {
        connectionId: currentConnection.id,
        userId: user.id,
        text: message,
      };
      stompClient.send('/api/v1/messages', {}, JSON.stringify(payload));
    } else {
      console.error('WebSocket is not connected');
    }
  };

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView(true);
  };

  const handleSendMessage = () => {
    if (message.trim().length === 0) return;
    emitMessage();
    setMessage('');
  };

  const onError = () => {};

  useEffect(() => {
    scrollToBottom();
  }, [messages.length]);

  useEffect(() => {
    if (user.id !== 0) {
      connect();
    } else {
      if (stompClient !== null) {
        stompClient.disconnect();
      }
    }
  }, [user.id]);

  useEffect(() => {
    if (currentConnection.id !== 0) {
      fetchChatMessages({ token, connectionId: currentConnection.id })
        .unwrap()
        .then((res) => {
          dispatch(setMessages(res.data));
        })
        .catch((err) => {
          console.log(err);
        });
    }
  }, [currentConnection.id, token]);

  return (
    <div className="flex flex-col justify-between min-h-[90vh] p-2">
      <div>
        <div>
          <h3 className="text-xl">
            {currentConnection.firstName} {currentConnection.lastName}
          </h3>
        </div>
        {isLoading && (
          <div>
            <Spinner message="Loading chat..." />
          </div>
        )}
        <div className="overflow-y-auto h-[600px]">
          <div className="my-4  p-2 flex flex-col-reverse">
            {messages.map((message) => {
              return (
                <div
                  className={`my-4 flex flex-col ${message.userId === user.id ? 'items-end' : 'items-start'}`}
                  key={message.id}
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
              );
            })}
          </div>
          <div ref={messagesEndRef}></div>
        </div>
      </div>
      <div>
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
    </div>
  );
};

export default Chat;
