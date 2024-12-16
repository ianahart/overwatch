import { useState } from 'react';
import DetailsModal from '../DetailsModal';

export interface IScreenshotProps {
  value: string;
}

const Screenshot = ({ value }: IScreenshotProps) => {
  const [isModalOpen, setIsModalOpen] = useState(false);

  return (
    <>
      {isModalOpen ? (
        <DetailsModal handleCloseModal={() => setIsModalOpen(false)}>
          <img src={value} alt="screenshot" />
        </DetailsModal>
      ) : (
        <img className="cursor-pointer" onClick={() => setIsModalOpen(true)} src={value} alt="screenshot" />
      )}
    </>
  );
};

export default Screenshot;
