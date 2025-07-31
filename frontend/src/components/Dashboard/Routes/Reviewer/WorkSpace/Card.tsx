import { LuGrip } from 'react-icons/lu';
import { useEffect, useState } from 'react';
import { CSS } from '@dnd-kit/utilities';
import { CiClock1 } from 'react-icons/ci';
import dayjs from 'dayjs';
import { useSelector } from 'react-redux';
import { AiOutlineEdit } from 'react-icons/ai';
import { useSortable } from '@dnd-kit/sortable';

import CardModal from './CardModal';
import { ITodoCard, IActiveLabel } from '../../../../../interfaces';
import { TRootState, useFetchActiveLabelsQuery } from '../../../../../state/store';

export interface ICardProps {
  data: ITodoCard;
}

const Card = ({ data }: ICardProps) => {
  const { attributes, listeners, setNodeRef, transform, transition } = useSortable({ id: `card-${data.id}` });
  const { token } = useSelector((store: TRootState) => store.user);

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isCardHovered, setIsCardHovered] = useState(false);
  const { data: activeLabelsData } = useFetchActiveLabelsQuery(
    { token, todoCardId: data.id },
    { skip: !token || !data.id }
  );
  const [activeLabel, setActiveLabel] = useState<IActiveLabel | null>(null);

  useEffect(() => {
    if (activeLabelsData !== undefined) {
      if (activeLabelsData.data.length > 0) {
        const latestActiveLabel = activeLabelsData.data.at(-1);
        if (latestActiveLabel !== undefined) {
          setActiveLabel(latestActiveLabel);
        }
      } else {
        setActiveLabel(null);
      }
    }
  }, [activeLabelsData]);

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
      data-testid="Card"
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
      {activeLabel !== null && (
        <div className="my-1 rounded p-1 inline-block" style={{ background: activeLabel.color }}>
          <p className="text-xs font-bold text-white">{activeLabel.title}</p>
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
            <AiOutlineEdit data-testid="card-edit-icon" />
          </div>
        )}
      </div>
      {isModalOpen && <CardModal card={data} handleOnModalClose={handleOnModalClose} />}
    </li>
  );
};

export default Card;
