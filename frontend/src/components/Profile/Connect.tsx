import { useEffect, useRef, useState } from 'react';
import { BsLightningCharge } from 'react-icons/bs';
import { useSelector } from 'react-redux';
import { over } from 'stompjs';
import SockJS from 'sockjs-client';
import { AiOutlineCheck } from 'react-icons/ai';

import { NotificationType, RequestStatus } from '../../enums';
import Avatar from '../Shared/Avatar';
import { TRootState, useCreateConnectionMutation, useVerifyConnectionQuery } from '../../state/store';

export interface IConnectProps {
  receiverId: number;
  senderId: number;
  fullName: string;
  avatarUrl: string;
  abbreviation: string;
}

let stompClient: any = null;

const Connect = ({ receiverId, senderId, fullName, avatarUrl, abbreviation }: IConnectProps) => {
  const shouldRun = useRef(true);
  const { token } = useSelector((store: TRootState) => store.user);
  const [error, setError] = useState('');
  const [createConnection, { isLoading }] = useCreateConnectionMutation();
  const { data } = useVerifyConnectionQuery({ token, receiverId, senderId });
  const [status, setStatus] = useState<RequestStatus>(RequestStatus.UNINITIATED);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const points = ['Stay in touch easier', 'Get to know one another', 'Get help when you need it'];

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
    console.log('WebSocket connected');
  };

  const onError = (err: any) => {
    console.error('WebSocket error:', err);
    // Reconnect after a delay
  };

  useEffect(() => {
    if (data) {
      setStatus(data.data);
    }
  }, [data]);

  useEffect(() => {
    if (shouldRun.current) {
      shouldRun.current = false;
      connect();
    }

    return () => {
      if (stompClient && stompClient.connected) {
        stompClient.disconnect(() => {
          console.log('WebSocket disconnected');
        });
      }
    };
  }, []);

  const emitNotification = () => {
    if (stompClient && stompClient.connected) {
      const payload = { receiverId, senderId, notificationType: NotificationType.CONNECTION_REQUEST_PENDING };
      stompClient.send('/api/v1/notify', {}, JSON.stringify(payload));
    } else {
      console.error('WebSocket is not connected');
    }
  };

  const handleConnectToReviewer = () => {
    if (status === RequestStatus.PENDING || status === RequestStatus.ACCEPTED) {
      return;
    }

    setError('');
    createConnection({ token, receiverId, senderId })
      .unwrap()
      .then(() => {
        emitNotification();
        setIsModalOpen(false);
      })
      .catch((err) => {
        setError(err.data.message);
      });
  };

  const renderConnectionStatus = () => {
    switch (status) {
      case RequestStatus.ACCEPTED:
        return 'Connected';
      case RequestStatus.PENDING:
        return 'Pending...';
      case RequestStatus.UNINITIATED:
        return 'Connect';
      default:
        return 'Connect';
    }
  };

  return (
    <div className="flex justify-end text-gray-400">
      {!isLoading && (
        <button onClick={() => setIsModalOpen(true)} className="btn flex items-center">
          <BsLightningCharge />
          {renderConnectionStatus()}
        </button>
      )}
      {isModalOpen && (
        <div className="absolute bg-black/80 top-0 left-0 w-full h-full min-h-full">
          <section className="bg-blue flex justify-center mt-56">
            <div className="bg-stone-950 max-w-full md:w-[600px] w-full m-1 p-4 rounded">
              <h3 className="text-xl text-center">Create a connection with {fullName}</h3>
              {error.length > 0 && (
                <div className="my-2 text-center">
                  <p className="text-sm text-red-300">{error}</p>
                </div>
              )}
              <div className="my-4 flex justify-center">
                <Avatar initials={abbreviation} width="w-10" height="h-10" avatarUrl={avatarUrl} />
              </div>
              <p className="my-2">To start the process of having your code reviewed make a connection</p>
              <ul className="ml-4 p-0 text-sm">
                {points.map((point, index) => (
                  <li key={index} className="flex items-center">
                    <AiOutlineCheck className="text-green-400 mr-1" />
                    <p>{point}</p>
                  </li>
                ))}
              </ul>
              <div className="flex justify-end">
                <button onClick={handleConnectToReviewer} className="outline-btn mx-1 bg-blue-400 font-bold">
                  Connect
                </button>
                <button onClick={() => setIsModalOpen(false)} className="outline-btn mx-1 bg-gray-400 font-bold">
                  Cancel
                </button>
              </div>
            </div>
          </section>
        </div>
      )}
    </div>
  );
};

export default Connect;
