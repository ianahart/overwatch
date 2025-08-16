import { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';

import { ITodoList } from '../../../../../interfaces';
import { TRootState, updateSingleTodoList, useEditTodoListMutation } from '../../../../../state/store';

export interface ITodoListTitleProps {
  list: ITodoList;
}

const TodoListTitle = ({ list }: ITodoListTitleProps) => {
  const dispatch = useDispatch();
  const { token } = useSelector((store: TRootState) => store.user);
  const [updateTodoList] = useEditTodoListMutation();
  const [inputValue, setInputValue] = useState(list.title);
  const [isInputShowing, setIsInputShowing] = useState(false);
  const [error, setError] = useState('');

  const applyServerErrors = <T extends object>(data: T) => {
    for (const [_, val] of Object.entries(data)) {
      setError(val);
    }
  };

  const handleUpdateTodoListTitle = () => {
    if (inputValue.toLowerCase() === list.title || inputValue.trim().length === 0) {
      return;
    }
    const payload = {
      id: list.id,
      title: inputValue,
      index: list.index,
      workSpaceId: list.workSpaceId,
      token,
    };

    updateTodoList(payload)
      .unwrap()
      .then((res) => {
        dispatch(updateSingleTodoList(res.data));
        setIsInputShowing(false);
      })
      .catch((err) => {
        applyServerErrors(err.data);
        console.log(err);
      });
  };

  const showInput = () => {
    setIsInputShowing(true);
  };

  const handleOnChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setInputValue(e.target.value);
  };

  const handleOnBlur = () => {
    handleUpdateTodoListTitle();
  };

  return (
    <>
      {!isInputShowing && (
        <div className="cursor-pointer" onClick={showInput}>
          <p>{list.title}</p>
        </div>
      )}
      {isInputShowing && (
        <div>
          <input
            onChange={handleOnChange}
            onBlur={handleOnBlur}
            value={inputValue}
            className="border bg-transparent border-gray-800 rounded h-9 w-full"
          />
          {error.length > 0 && (
            <div>
              <p className="text-xs text-red-300 my-1">{error}</p>
            </div>
          )}
        </div>
      )}
    </>
  );
};

export default TodoListTitle;
