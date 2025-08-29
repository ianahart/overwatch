import { useEffect, useMemo, useState } from 'react';
import { BsArrowRight } from 'react-icons/bs';
import { AiOutlineClose } from 'react-icons/ai';
import { useDispatch, useSelector } from 'react-redux';

import ClickAway from '../../../../../Shared/ClickAway';
import { ITodoCard, ITodoList } from '../../../../../../interfaces';
import { TRootState, moveTodoCard, useMoveTodoCardsMutation } from '../../../../../../state/store';

export interface ICardMoveBtnProps {
  card: ITodoCard;
}

const CardMoveBtn = ({ card }: ICardMoveBtnProps) => {
  const dispatch = useDispatch();
  const { token } = useSelector((store: TRootState) => store.user);
  const { todoLists } = useSelector((store: TRootState) => store.todoList);
  const [moveTodoCardsMut] = useMoveTodoCardsMutation();
  const [clickAwayOpen, setClickAwayOpen] = useState(false);
  const [newIndex, setNewIndex] = useState(0);
  const [destinationList, setDestinationList] = useState<ITodoList | null>(null);
  const [destinationListId, setDestinationListId] = useState<number>(() => {
    return todoLists.find((todoList) => todoList.id === card.todoListId)?.id ?? 0;
  });

  const sourceTodoList = useMemo(() => {
    const currentTodoList = todoLists.find((todoList) => todoList.id === card.todoListId);
    if (currentTodoList !== undefined) {
      return {
        ...currentTodoList,
        todoCards: currentTodoList.cards,
      };
    }
  }, [card.todoListId]);

  const handleOnClick = () => {
    if (sourceTodoList && destinationListId) {
      handleMoveTodoCards(sourceTodoList.id, destinationListId, card.id, newIndex);
      handleOnClickAwayClose();
    }
  };

  const handleMoveTodoCards = (
    sourceListId: number,
    destinationListId: number,
    todoCardId: number,
    newIndex: number
  ) => {
    moveTodoCardsMut({ token, sourceListId, destinationListId, todoCardId, newIndex })
      .unwrap()
      .then(() => {
        dispatch(
          moveTodoCard({
            sourceListId,
            destinationListId,
            cardId: todoCardId,
            newIndex,
          })
        );
      })
      .catch((err) => {
        console.log(err);
      });
  };

  useEffect(() => {
    setNewIndex(card.index);
  }, [card.index]);

  useEffect(() => {
    if (destinationListId !== null) {
      setDestinationList(todoLists.find((todoList) => todoList.id === destinationListId) ?? null);
    }
  }, [destinationListId, todoLists]);

  const handleOnClickAwayOpen = () => setClickAwayOpen(true);

  const handleOnClickAwayClose = () => setClickAwayOpen(false);

  return (
    <div>
      <div className="p-1 bg-gray-800 rounded w-32 my-2 hover:bg-gray-700 relative">
        <button onClick={handleOnClickAwayOpen} className="flex items-center text-sm">
          <BsArrowRight className="mr-2" />
          Move card
        </button>
        {clickAwayOpen && (
          <ClickAway onClickAway={handleOnClickAwayClose}>
            <div className="p-2 rounded bg-gray-700 overflow-x-hidden absolute z-20 right-0 w-[250px] h-56 overflow-y-auto top-0">
              <div className="flex justify-between">
                <div>&nbsp;</div>
                <h3 className="text-center">Move card</h3>
                <AiOutlineClose
                  data-testid="move-card-close-btn"
                  onClick={handleOnClickAwayClose}
                  className="cursor-pointer hover:opacity-70"
                />
              </div>
              <div className="text-xs font-bold mb-1 mt-2">
                <p>Select a destination</p>
              </div>
              <div className="p-1 bg-gray-800 rounded">
                <p className="font-bold">{card.title}</p>
              </div>
              <div className="flex justify-between">
                <div className="flex flex-col">
                  <label className="text-xs my-1" htmlFor="list">
                    Move To
                  </label>
                  <select
                    data-testid="move-card-select"
                    onChange={(e) => setDestinationListId(Number.parseInt(e.target.value))}
                    value={destinationListId}
                    className="h-9 rounded bg-transparent border border-gray-600"
                    id="list"
                    name="list"
                  >
                    {todoLists.map((todoList) => {
                      return (
                        <option key={todoList.id} value={todoList.id}>
                          {todoList.title}
                        </option>
                      );
                    })}
                  </select>
                </div>
                <div className="flex flex-col">
                  <label className="text-xs my-1" htmlFor="position">
                    At position
                  </label>
                  {destinationList !== null && (
                    <select
                      onChange={(e) => setNewIndex(Number.parseInt(e.target.value))}
                      className="h-9 rounded bg-transparent border border-gray-600"
                      value={newIndex}
                      id="position"
                      name="position"
                    >
                      <>
                        {destinationList?.cards?.map((todoCard, index) => {
                          return (
                            <option value={index} key={todoCard.id}>
                              {index}
                            </option>
                          );
                        })}
                      </>
                    </select>
                  )}
                </div>
              </div>
              <div className="my-4 justify-center flex">
                <button onClick={handleOnClick} className="btn">
                  Save
                </button>
              </div>
            </div>
          </ClickAway>
        )}
      </div>
    </div>
  );
};
export default CardMoveBtn;
