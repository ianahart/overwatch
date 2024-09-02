import { LuGrip } from 'react-icons/lu';
import { ITodoCard } from '../../../../../interfaces';
import { useState } from 'react';
import CardModal from './CardModal';
import { AiOutlineEdit } from 'react-icons/ai';

export interface ICardProps {
  data: ITodoCard;
}

const Card = ({ data }: ICardProps) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isCardHovered, setIsCardHovered] = useState(false);

  const handleOnModalOpen = () => {
    setIsModalOpen(true);
  };

  const handleOnModalClose = () => {
    setIsModalOpen(false);
  };

  const handleOnMouseLeave = () => {
    setIsCardHovered(false);
  };

  const handleOnMouseEnter = () => {
    setIsCardHovered(true);
  };

  return (
    <li onClick={handleOnModalOpen} className="my-6 bg-gray-800 hover:bg-gray-700 p-2 rounded shadow-md cursor-pointer">
      <div className="flex justify-end">
        <LuGrip />
      </div>
      <div
        onMouseLeave={handleOnMouseLeave}
        onMouseEnter={handleOnMouseEnter}
        className="flex items-center justify-between"
      >
        <p>{data.title}</p>
        {isCardHovered && (
          <div>
            <AiOutlineEdit />
          </div>
        )}
      </div>
      {isModalOpen && <CardModal card={data} handleOnModalClose={handleOnModalClose} />}
    </li>
  );
};

export default Card;
