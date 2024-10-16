import { useSelector } from 'react-redux';
import { FaLongArrowAltRight, FaRegCircle } from 'react-icons/fa';
import { useState } from 'react';
import { BsPinAngle } from 'react-icons/bs';

import { IConnection, IPinnedConnection } from '../../../interfaces';
import Avatar from '../../Shared/Avatar';
import { TRootState } from '../../../state/store';
import { retrieveTokens, shortenString } from '../../../util';
import { MdBlock } from 'react-icons/md';

type TConnection = IConnection | IPinnedConnection;

export interface IConnectionProps {
  connection: TConnection;
  changeConnection: (connection: IConnection) => void;
  isPinned: boolean;
  unPin: (id: number) => void;
  pin: (ownerId: number, connectionId: number, pinnedId: number, token: string, connection: IConnection) => void;
  blockUser?: (blockerUserId: number, blockedUserId: number, connection: IConnection) => void;
}

const Connection = ({ connection, changeConnection, isPinned, unPin, pin, blockUser }: IConnectionProps) => {
  const { currentConnection } = useSelector((store: TRootState) => store.chat);
  const { user } = useSelector((store: TRootState) => store.user);
  const [toolTipShowing, setToolTipShowing] = useState({ pin: false, block: false });

  const isPinnedConnection = (connection: TConnection): connection is IPinnedConnection => {
    return 'connectionPinId' in connection;
  };

  const handleOnMouseEnter = (toolTip: string): void => {
    setToolTipShowing((prevState) => ({
      ...prevState,
      [toolTip]: true,
    }));
  };

  const handleOnMouseLeave = (toolTip: string): void => {
    setToolTipShowing((prevState) => ({
      ...prevState,
      [toolTip]: false,
    }));
  };

  const handleOnBlock = (connection: IConnection): void => {
    const blockerUserId = user.id === connection.receiverId ? connection.receiverId : connection.senderId;
    const blockedUserId = blockerUserId === connection.senderId ? connection.receiverId : connection.senderId;
    blockUser?.(blockerUserId, blockedUserId, connection);
  };

  const handleOnPin = (connection: IConnection): void => {
    const ownerId = user.id === connection.receiverId ? connection.receiverId : connection.senderId;
    const pinnedId = connection.receiverId === ownerId ? connection.senderId : connection.receiverId;
    pin(ownerId, connection.id, pinnedId, retrieveTokens().token, connection);
  };

  const handleOnUnPin = () => {
    if (isPinned && isPinnedConnection(connection)) {
      unPin(connection.connectionPinId);
    }
  };

  return (
    <div
      className={`my-4 p-1 rounded hover:bg-stone-950 hover:text-gray-400 cursor-pointer ${
        currentConnection.id === connection.id ? 'bg-green-400 text-black' : 'bg-transparent text-gray-400'
      }`}
    >
      <div className="flex justify-between">
        <div className="flex items-center" onClick={() => changeConnection(connection)}>
          <Avatar initials="?.?" width="h-9" height="h-9" avatarUrl={connection.avatarUrl} />
          <p className="ml-2">
            {connection.firstName} {connection.lastName}
          </p>
        </div>
        <div className="flex items-center">
          <div className="relative mx-1">
            <MdBlock
              className="text-xl"
              onClick={() => handleOnBlock(connection)}
              onMouseEnter={() => handleOnMouseEnter('block')}
              onMouseLeave={() => handleOnMouseLeave('block')}
            />
            {toolTipShowing.block && (
              <div className="absolute -top-8 left-0 bg-stone-900 p-2 rounded">
                <p className="text-xs">Block</p>
              </div>
            )}
          </div>
          <div className="relative mx-1">
            {isPinned ? (
              <BsPinAngle onClick={handleOnUnPin} />
            ) : (
              <FaRegCircle
                onClick={() => handleOnPin(connection)}
                onMouseEnter={() => handleOnMouseEnter('pin')}
                onMouseLeave={() => handleOnMouseLeave('pin')}
              />
            )}
            {toolTipShowing.pin && (
              <div className="absolute -top-8 left-0 bg-stone-900 p-2 rounded">
                <p className="text-xs">Pin</p>
              </div>
            )}
          </div>
        </div>
      </div>
      <div>
        <p className="text-xs flex items-center">
          {connection.lastMessage.length > 0 && <FaLongArrowAltRight className="mr-1 text-gray-500" />}
          {shortenString(connection.lastMessage ? connection.lastMessage : '')}
        </p>
      </div>
    </div>
  );
};

export default Connection;
