import { useSelector } from 'react-redux';
import { AiOutlinePlus } from 'react-icons/ai';

import { ITeam } from '../../../../interfaces';
import { TRootState } from '../../../../state/store';
import { useState } from 'react';
import TeamModal from '../../TeamModal';
import MessageForm from './MessageForm';

export interface ITeamPinnedMessageProps {
  team: ITeam;
}

const TeamPinnedMessage = ({ team }: ITeamPinnedMessageProps) => {
  const { user } = useSelector((store: TRootState) => store.user);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const openModal = (): void => {
    setIsModalOpen(true);
  };

  const closeModal = (): void => {
    setIsModalOpen(false);
  };

  return (
    <div className="flex md:flex-row flex-col md:items-center md:justify-between">
      <div className=" max-w-[600px] mx-auto">
        <div>
          <h3 className="text-xl">Pinned Messages</h3>
          <p>These are messages pinned by the admin of your team.</p>
        </div>
      </div>
      {team.userId === user.id && (
        <>
          <div className="btn">
            <button onClick={openModal} className="flex items-center text-sm">
              <AiOutlinePlus />
              New Message
            </button>
          </div>
          {isModalOpen && (
            <div>
              <TeamModal closeModal={closeModal}>
                <MessageForm closeModal={closeModal} team={team} formType="create" />
              </TeamModal>
            </div>
          )}
        </>
      )}
    </div>
  );
};

export default TeamPinnedMessage;
