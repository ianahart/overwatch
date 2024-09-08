import { LuGrip } from 'react-icons/lu';
import { useState } from 'react';
import { CSS } from '@dnd-kit/utilities';

import { AiOutlineEdit } from 'react-icons/ai';
import { useSortable } from '@dnd-kit/sortable';

import CardModal from './CardModal';
import { ITodoCard } from '../../../../../interfaces';
import { CiClock1 } from 'react-icons/ci';
import dayjs from 'dayjs';

export interface ICardProps {
  data: ITodoCard;
}

const Card = ({ data }: ICardProps) => {
  const { attributes, listeners, setNodeRef, transform, transition } = useSortable({ id: `card-${data.id}` });

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

  const style = {
    transition,
    transform: CSS.Translate.toString(transform),
  };

  return (
    <li
      ref={setNodeRef}
      style={!isModalOpen ? style : {}}
      {...attributes}
      {...listeners}
      onClick={handleOnModalOpen}
      className="my-6 bg-gray-800 hover:bg-gray-700 p-2 rounded shadow-md cursor-pointer"
    >
      <div className="flex justify-between">
        {data.photo?.length > 0 ? (
          <img className="h-9 w-[90%] rounded" src={data.photo} alt={data.title} />
        ) : (
          <div></div>
        )}
        <LuGrip />
      </div>
      {data.startDate !== null && data.endDate !== null && (
        <div className="my-1 flex items-center">
          <div className="mr-1">
            <CiClock1 />
          </div>
          <div>
            <p className="text-xs">
              {dayjs(data.startDate).format('MMM D')}-{dayjs(data.endDate).format('MMM D')}
            </p>
          </div>
        </div>
      )}
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
