import { AiOutlineClose } from 'react-icons/ai';

export interface ITeamModalProps {
  children: React.ReactNode;
  closeModal: () => void;
}

const TeamModal = ({ children, closeModal }: ITeamModalProps) => {
  const handleCloseModal = (e: React.MouseEvent<HTMLDivElement>): void => {
    e.stopPropagation();
    closeModal();
  };
  return (
    <div data-testid="team-modal" className="fixed inset-0 bg-gray-800 bg-opacity-90">
      <div className="flex items-center flex-col justify-center min-h-[60vh]">
        <div className="max-w-[600px] bg-gray-900 rounded shadow-lg p-2 w-full">
          <div className="m-2 flex justify-end">
            <div
              data-testid="team-modal-close"
              onClick={handleCloseModal}
              className="bg-black p-2 rounded-3xl cursor-pointer"
            >
              <AiOutlineClose className="text-4xl text-white" />
            </div>
          </div>
          {children}
        </div>
      </div>
    </div>
  );
};

export default TeamModal;
