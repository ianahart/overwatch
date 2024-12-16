import { useState } from 'react';
import DetailsModal from '../DetailsModal';
import { shortenString } from '../../../../../util';

export interface IDescriptionProps {
  value: string;
}

const Description = ({ value }: IDescriptionProps) => {
  const [isModalOpen, setIsModalOpen] = useState(false);

  return (
    <>
      {isModalOpen ? (
        <DetailsModal handleCloseModal={() => setIsModalOpen(false)}>
          <p>{value}</p>
        </DetailsModal>
      ) : (
        <p onClick={() => setIsModalOpen(true)} className="cursor-pointer">
          {shortenString(value, 10)}
        </p>
      )}
    </>
  );
};

export default Description;
