import { BsWindowDesktop } from 'react-icons/bs';
import { AiOutlineClose } from 'react-icons/ai';
import { useDispatch, useSelector } from 'react-redux';

import { ITodoCard } from '../../../../../interfaces';
import { useState } from 'react';
import { TPureTodoCard } from '../../../../../types';
import { TRootState, updateTodoListTodoCard, useUpdateTodoCardMutation } from '../../../../../state/store';

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
    <div className="flex justify-between">
      <div className="flex flex-grow-[2]">
        <div className="mr-2 mt-1">
          <BsWindowDesktop className="text-2xl" />
        </div>
        <div className="flex-grow-[2]">
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
          <p>
            In list <span className="underline">{card.todoListTitle}</span>
          </p>
          {card.photo !== null && card.photo?.length > 0 && (
            <img className="h-20 w-[200px] rounded" src={card.photo} alt={card.title} />
          )}
        </div>
      </div>
      <div onClick={handleOnClick} className="rounded h-9 border border-gray-800 cursor-pointer">
        <AiOutlineClose className="text-2xl m-1" />
      </div>
    </div>
  );
};

export default CardHeader;
