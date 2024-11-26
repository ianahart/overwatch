import { useState } from 'react';
import { AiOutlinePlus } from 'react-icons/ai';
import TeamModal from './TeamModal';
import CreateTeamForm from './CreateTeamForm';

const CreateTeamBtn = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);

  const closeModal = (): void => {
    setIsModalOpen(false);
  };

  const openModal = (): void => {
    setIsModalOpen(true);
  };
  return (
    <>
      <button
        onClick={openModal}
        className="my-8 items-center cursor-pointer rounded p-1 border inline-flex border-gray-800 hover:text-gray-500"
      >
        <AiOutlinePlus className="mr-1" />
        Create Team
      </button>
      {isModalOpen && (
        <TeamModal closeModal={closeModal}>
          <CreateTeamForm closeModal={closeModal} />
        </TeamModal>
      )}
    </>
  );
};

export default CreateTeamBtn;
