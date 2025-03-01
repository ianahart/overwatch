import dayjs from 'dayjs';
import { useSelector } from 'react-redux';
import { useState } from 'react';
import { RiPushpinFill } from 'react-icons/ri';
import { BiSolidEditAlt } from 'react-icons/bi';

import { ITeam, ITeamPinnedMessage, IUser } from '../../../../interfaces';
import { initializeName } from '../../../../util';
import Avatar from '../../../Shared/Avatar';
import ToolTip from '../../../Shared/ToolTip';
import TeamModal from '../../TeamModal';
import MessageForm from './MessageForm';
import { TRootState, useDeleteTeamPinnedMessageMutation } from '../../../../state/store';

export interface IMessageListItemProps {
  team: ITeam;
  message: ITeamPinnedMessage;
  user: IUser;
}

const MessageListItem = ({ message, user, team }: IMessageListItemProps) => {
  const { token } = useSelector((store: TRootState) => store.user);
  const [first, last] = message.fullName.split(' ');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [deleteMessageMut] = useDeleteTeamPinnedMessageMutation();

  const openModal = (): void => {
    setIsModalOpen(true);
  };
  const closeModal = (): void => {
    setIsModalOpen(false);
  };

  const handleOnDeleteMessage = () => {
    const payload = { teamId: team.id, teamPinnedMessageId: message.id, token };
    deleteMessageMut(payload)
      .unwrap()
      .then(() => {})
      .catch((err) => {
        console.log(err);
      });
  };

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
      {user.id === team.userId && (
        <div className="flex justify-end">
          <div className="flex">
            <ToolTip message="Edit">
              <BiSolidEditAlt onClick={openModal} className="mx-2 text-xl cursor-pointer" />
            </ToolTip>
            <ToolTip message="UnPin">
              <RiPushpinFill onClick={handleOnDeleteMessage} className="mx-2 text-xl cursor-pointer" />
            </ToolTip>
          </div>
        </div>
      )}
      {isModalOpen && (
        <TeamModal closeModal={closeModal}>
          <MessageForm
            teamPinnedMessageId={message.id}
            formType="update"
            team={team}
            closeModal={closeModal}
            message={message.message}
          />
        </TeamModal>
      )}
    </div>
  );
};

export default MessageListItem;
