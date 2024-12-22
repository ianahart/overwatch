import { BsLightningCharge } from 'react-icons/bs';
import { useEffect, useState } from 'react';

import {  useDeleteConnectionMutation, useVerifyConnectionQuery } from '../../state/store';
import { RequestStatus } from '../../enums';
import {retrieveTokens} from '../../util';

export interface IDisconnectProps {
  receiverId: number;
  senderId: number;
}

const Disconnect = ({ receiverId, senderId }: IDisconnectProps) => {
  const token = retrieveTokens().token;
  const { data } = useVerifyConnectionQuery({ token, receiverId, senderId });
  const [deleteConnection] = useDeleteConnectionMutation();
  const [status, setStatus] = useState<RequestStatus>(RequestStatus.UNINITIATED);
  const [connectionId, setConnectionId] = useState(0);

  useEffect(() => {
    if (data) {
      setStatus(data.data.status);
      setConnectionId(data.data.id);
    }
  }, [data]);

  const handleDisconnect = () => {
    deleteConnection({ token, connectionId })
      .unwrap()
      .then(() => {
        setStatus(RequestStatus.UNINITIATED);
        setConnectionId(0);
      });
  };

  return (
    <div className="flex justify-end">
      {status === RequestStatus.ACCEPTED && connectionId !== 0 && (
        <button onClick={handleDisconnect} className="btn flex items-center">
          <BsLightningCharge />
          Connected
        </button>
      )}
    </div>
  );
};

export default Disconnect;
