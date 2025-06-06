import { useEffect, useRef, useState } from 'react';
import { BsLightningCharge } from 'react-icons/bs';
import { AiOutlineCheck } from 'react-icons/ai';

import { NotificationType, RequestStatus } from '../../enums';
import Avatar from '../Shared/Avatar';
import { connectWebSocket, disconnectWebSocket, sendMessage } from '../../util/WebSocketService';
import { useCreateConnectionMutation, useDeleteConnectionMutation, useVerifyConnectionQuery } from '../../state/store';
import { retrieveTokens } from '../../util';

export interface IConnectProps {
  receiverId: number;
  senderId: number;
  fullName: string;
  avatarUrl: string;
  abbreviation: string;
}

const Connect = ({ receiverId, senderId, fullName, avatarUrl, abbreviation }: IConnectProps) => {
  const shouldRun = useRef(true);
  const token = retrieveTokens().token;
  const [error, setError] = useState('');
  const [createConnection, { isLoading }] = useCreateConnectionMutation();
  const [deleteConnection] = useDeleteConnectionMutation();
  const { data } = useVerifyConnectionQuery(
    { token, receiverId, senderId },
    { skip: !receiverId || !senderId || !token }
  );
  const [status, setStatus] = useState<RequestStatus>(RequestStatus.UNINITIATED);
  const [connectionId, setConnectionId] = useState(0);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const points = ['Stay in touch easier', 'Get to know one another', 'Get help when you need it'];

  const onConnected = () => {
    console.log('WebSocket connected');
  };

  const onError = (err: any) => {
    console.error('WebSocket error:', err);
  };

  useEffect(() => {
    if (data) {
      setStatus(data.data.status);
      setConnectionId(data.data.id);
    }
  }, [data]);

  useEffect(() => {
    if (shouldRun.current) {
      shouldRun.current = false;
      connectWebSocket(onConnected, onError);
    }
    return () => {
      disconnectWebSocket();
    };
  }, []);

  const emitNotification = () => {
    const payload = { receiverId, senderId, notificationType: NotificationType.CONNECTION_REQUEST_PENDING };
    sendMessage('/api/v1/notify', JSON.stringify(payload));
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

  const disconnect = () => {
    deleteConnection({ token, connectionId })
      .unwrap()
      .then(() => {
        setStatus(RequestStatus.UNINITIATED);
        setIsModalOpen(false);
        setConnectionId(0);
      });
  };

  return (
    <div className="flex justify-end text-gray-400">
      {!isLoading && (
        <button
          data-testid="connect-modal-details"
          onClick={() => setIsModalOpen(true)}
          className="btn flex items-center"
        >
          <BsLightningCharge />
          {renderConnectionStatus()}
        </button>
      )}
      {isModalOpen && (
        <div className="absolute bg-black/80 top-0 left-0 w-full h-full min-h-full">
          <section className="bg-blue flex justify-center mt-56">
            <div className="bg-stone-950 max-w-full md:w-[600px] w-full m-1 p-4 rounded">
              {RequestStatus.ACCEPTED === status ? (
                <div>
                  <p>Do you want to disconnect with {fullName}?</p>
                  <div className="flex justify-end">
                    <button
                      data-testid="connect-disconnect-btn"
                      onClick={disconnect}
                      className="outline-btn mx-1 bg-blue-400 font-bold"
                    >
                      Disconnect
                    </button>
                    <button
                      data-testid="connect-disconnect-cancel-btn"
                      onClick={() => setIsModalOpen(false)}
                      className="outline-btn mx-1 bg-gray-400 font-bold"
                    >
                      Cancel
                    </button>
                  </div>
                </div>
              ) : (
                <>
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
                        <AiOutlineCheck data-testid="connect-points-check" className="text-green-400 mr-1" />
                        <p>{point}</p>
                      </li>
                    ))}
                  </ul>
                  <div className="flex justify-end">
                    <button
                      data-testid="connect-new-connect-btn"
                      onClick={handleConnectToReviewer}
                      className="outline-btn mx-1 bg-blue-400 font-bold"
                    >
                      Connect
                    </button>
                    <button
                      data-testid="connect-new-cancel-btn"
                      onClick={() => setIsModalOpen(false)}
                      className="outline-btn mx-1 bg-gray-400 font-bold"
                    >
                      Cancel
                    </button>
                  </div>
                </>
              )}
            </div>
          </section>
        </div>
      )}
    </div>
  );
};

export default Connect;
