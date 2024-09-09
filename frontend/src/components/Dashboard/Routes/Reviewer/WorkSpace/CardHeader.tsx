import { BsWindowDesktop } from 'react-icons/bs';
import { AiOutlineClose } from 'react-icons/ai';
import { useDispatch, useSelector } from 'react-redux';
import dayjs from 'dayjs';
import { CiClock1 } from 'react-icons/ci';

import { IActiveLabel, ITodoCard } from '../../../../../interfaces';
import { useEffect, useState } from 'react';
import { TPureTodoCard } from '../../../../../types';
import {
  TRootState,
  updateTodoListTodoCard,
  useFetchActiveLabelsQuery,
  useUpdateTodoCardMutation,
} from '../../../../../state/store';
import CardOptions from './Actions/CardOptions';
import CardActions from './Actions/CardActions';

export interface ICardHeaderProps {
  card: ITodoCard;
  handleOnModalClose: () => void;
}

const CardHeader = ({ card, handleOnModalClose }: ICardHeaderProps) => {
  const dispatch = useDispatch();
  const { token } = useSelector((store: TRootState) => store.user);
  const [updateTodoCard] = useUpdateTodoCardMutation();
  const [showInput, setShowInput] = useState(false);
  const [title, setTitle] = useState(card.title);
  const [activeLabels, setActiveLabels] = useState<IActiveLabel[]>([]);
  const { data } = useFetchActiveLabelsQuery({ token, todoCardId: card.id });

  useEffect(() => {
    if (data !== undefined) {
      setActiveLabels(data.data);
    }
  }, [data]);

  const handleOnClick = (e: React.MouseEvent<HTMLDivElement>) => {
    e.stopPropagation();
    handleOnModalClose();
  };

  const handleOnShowInput = () => {
    setShowInput(true);
  };

  const handleOnBlur = () => {
    setShowInput(false);
    if (title.trim().length === 0) {
      setTitle(card.title);
      return;
    }
    handleUpdateCard(title);
  };

  const handleUpdateCard = (title: string) => {
    const payload: TPureTodoCard = {
      title,
      createdAt: card.createdAt,
      index: card.index,
      photo: card.photo,
      label: card.label,
      color: card.color,
      details: card.details,
      startDate: card.startDate,
      endDate: card.endDate,
      id: card.id,
    };
    updateTodoCard({ token, card: payload })
      .unwrap()
      .then((res) => {
        setTitle('');
        dispatch(updateTodoListTodoCard(res.data));
      });
  };

  const handleOnChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setTitle(e.target.value);
  };

  const handleOnKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    e.stopPropagation();
  };

  return (
    <div>
      <div className="mr-2 mt-1 flex justify-between">
        <div className="flex items-center">
          <BsWindowDesktop className="text-2xl mr-2" />
          {!showInput ? (
            <h3 onClick={handleOnShowInput} className="text-2xl h-9 hover:cursor-text">
              {card.title}
            </h3>
          ) : (
            <input
              className="border border-gray-800 bg-transparent h-9 rounded w-[90%]"
              onKeyDown={handleOnKeyDown}
              onChange={handleOnChange}
              onBlur={handleOnBlur}
              value={title}
            />
          )}
        </div>
        <div onClick={handleOnClick} className="rounded h-9 border border-gray-800 cursor-pointer">
          <AiOutlineClose className="text-2xl m-1" />
        </div>
      </div>
      <div className="flex justify-between">
        <div className="flex-grow-[2] mr-8">
          <p>
            In list <span className="underline">{card.todoListTitle}</span>
          </p>
          {card.startDate !== null && card.endDate !== null && (
            <div className="my-1 flex items-center">
              <div className="mr-2">
                <CiClock1 className="text-xl" />
              </div>
              <p>
                {dayjs(card.startDate).format('MMM D')}-{dayjs(card.endDate).format('MMM D')}
              </p>
            </div>
          )}
          {card.photo !== null && card.photo?.length > 0 && (
            <img className="h-20 w-[200px] rounded" src={card.photo} alt={card.title} />
          )}
          <div className="flex flex-wrap items-center">
            {activeLabels.map((activeLabel) => {
              return (
                <div className="p-1 rounded m-1" style={{ background: activeLabel.color }} key={activeLabel.id}>
                  <p className="text-xs font-bold text-white">{activeLabel.title}</p>
                </div>
              );
            })}
          </div>
        </div>
        <div className="my-8 flex flex-col items-end">
          <div className="my-4">
            <CardOptions card={card} />
          </div>
          <div className="my-4">
            <CardActions card={card} />
          </div>
        </div>
      </div>
    </div>
  );
};

export default CardHeader;
