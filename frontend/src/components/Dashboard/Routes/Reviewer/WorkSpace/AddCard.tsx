import { useState } from 'react';
import { AiOutlineClose, AiOutlinePlus } from 'react-icons/ai';
import { useDispatch, useSelector } from 'react-redux';
import { TRootState, addCardToTodoList, useCreateTodoCardMutation } from '../../../../../state/store';
import { ICreateTodoCardRequest, ITodoList } from '../../../../../interfaces';

export interface IAddCardProps {
  todoList: ITodoList;
}

const AddCard = ({ todoList }: IAddCardProps) => {
  const dispatch = useDispatch();
  const { token, user } = useSelector((store: TRootState) => store.user);
  const [createTodoCard] = useCreateTodoCardMutation();
  const [isFormShowing, setIsFormShowing] = useState(false);
  const [inputValue, setInputValue] = useState('');
  const [error, setError] = useState('');

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (inputValue.trim().length === 0) {
      return;
    }
    const index = todoList.cards.length - 1 === -1 ? 0 : todoList.cards.length;

    const payload = { token, userId: user.id, todoListId: todoList.id, index, title: inputValue };
    handleCreateTodoCard(payload);
  };

  const applyServerErrors = <T extends object>(data: T) => {
    for (const [_, val] of Object.entries(data)) {
      setError(val);
    }
  };

  const handleCreateTodoCard = (payload: ICreateTodoCardRequest) => {
    setError('');
    createTodoCard(payload)
      .unwrap()
      .then((res) => {
        dispatch(addCardToTodoList(res.data));
        setIsFormShowing(false);
        setInputValue('');
      })
      .catch((err) => {
        applyServerErrors(err.data);
      });
  };

  const handleOnChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setInputValue(e.target.value);
  };

  const handleOnClickShowForm = () => {
    setIsFormShowing(true);
  };

  const handleOnClickHideForm = () => {
    setIsFormShowing(false);
  };

  const handleOnKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    e.stopPropagation();
  };

  return (
    <div className="my-2 flex items-center p-1 rounded cursor-pointer">
      {!isFormShowing && (
        <button onClick={handleOnClickShowForm} className="flex items-center hover:opacity-70">
          <AiOutlinePlus className="mr-2" />
          <p>Add card</p>
        </button>
      )}
      {isFormShowing && (
        <form onSubmit={handleOnSubmit} className="w-full">
          <div>
            <input
              onKeyDown={handleOnKeyDown}
              type="text"
              onChange={handleOnChange}
              value={inputValue}
              placeholder="Enter card name..."
              className="w-full h-9 bg-transparent rounded border border-gray-800"
            />

            {error.length > 0 && <p className="text-red-400 text-xs my-2">{error}</p>}
          </div>
          <div className="my-2 flex items-center justify-between">
            <button type="submit" className="btn">
              Add card
            </button>
            <AiOutlineClose onClick={handleOnClickHideForm} className="text-xl" />
          </div>
        </form>
      )}
    </div>
  );
};
export default AddCard;
