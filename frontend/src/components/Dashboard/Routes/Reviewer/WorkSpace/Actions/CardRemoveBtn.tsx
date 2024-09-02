import { BsTrash } from 'react-icons/bs';
import { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';

import { ITodoCard } from '../../../../../../interfaces';
import ClickAway from '../../../../../Shared/ClickAway';
import { TRootState, removeTodoListTodoCard, useDeleteTodoCardMutation } from '../../../../../../state/store';

export interface ICardRemoveBtnProps {
  card: ITodoCard;
}

const CardRemoveBtn = ({ card }: ICardRemoveBtnProps) => {
  const dispatch = useDispatch();
  const { token } = useSelector((store: TRootState) => store.user);
  const [deleteTodoCard] = useDeleteTodoCardMutation();
  const [clickAwayOpen, setClickAwayOpen] = useState(false);
  const [error, setError] = useState('');

  const handleOnClickAwayOpen = () => {
    setClickAwayOpen(true);
  };

  const handleOnClickAwayClose = () => {
    setClickAwayOpen(false);
  };

  const handleOnClickRemove = () => {
    setError('');
    deleteTodoCard({ token, todoCardId: card.id })
      .unwrap()
      .then((res) => {
        console.log(res);
        dispatch(removeTodoListTodoCard({ id: card.id, todoListId: card.todoListId }));
      })
      .catch((err) => {
        setError(err.data?.message);
      });
  };

  return (
    <div className="relative">
      <div className="p-1 bg-gray-800 rounded w-32 my-2 hover:bg-gray-700">
        <button onClick={handleOnClickAwayOpen} className="flex items-center text-sm">
          <BsTrash className="mr-2" />
          Remove
        </button>
      </div>
      {clickAwayOpen && (
        <ClickAway onClickAway={handleOnClickAwayClose}>
          <div className="p-2 rounded bg-gray-700 absolute z-20 right-0 w-[200px] top-0">
            {error.length > 0 ? (
              <p className="text-sm text-red-300">{error}</p>
            ) : (
              <p className="text-sm">Are you sure you want to remove this card?</p>
            )}
            <div className="flex my-1 justify-between">
              <button onClick={handleOnClickRemove} className="btn">
                Yes
              </button>
              <button onClick={handleOnClickAwayClose} className="btn">
                Cancel
              </button>
            </div>
          </div>
        </ClickAway>
      )}
    </div>
  );
};

export default CardRemoveBtn;
