import { useState } from 'react';
import { AiOutlineClose, AiOutlinePlus } from 'react-icons/ai';
import { useDispatch, useSelector } from 'react-redux';
import { TRootState, addToTodoList, useCreateTodoListMutation } from '../../../../../state/store';

const AddTodoList = () => {
  const dispatch = useDispatch();
  const { user, token } = useSelector((store: TRootState) => store.user);
  const { workSpace } = useSelector((store: TRootState) => store.workSpace);
  const { todoLists } = useSelector((store: TRootState) => store.todoList);
  const [createTodoList] = useCreateTodoListMutation();
  const [isDropDownOpen, setIsDropDownOpen] = useState(false);
  const [inputValue, setInputValue] = useState('');
  const [error, setError] = useState('');

  const closeDropDown = () => {
    setIsDropDownOpen(false);
  };

  const openDropDown = () => {
    setIsDropDownOpen(true);
  };

  const applyServerErrors = <T extends object>(data: T) => {
    for (const [_, val] of Object.entries(data)) {
      setError(val);
    }
  };

  const handleOnChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setInputValue(e.target.value);
  };

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError('');
    if (inputValue.trim().length === 0) {
      return;
    }

    const nextIndex = todoLists.length - 1 === -1 ? 0 : todoLists.length;
    const payload = {
      userId: user.id,
      title: inputValue,
      workSpaceId: workSpace.id,
      token,
      index: nextIndex,
    };
    createTodoList(payload)
      .unwrap()
      .then((res) => {
        dispatch(addToTodoList(res.data));

        closeDropDown();
      })
      .catch((err) => {
        applyServerErrors(err.data);
      });
  };

  return (
    <>
      <div className="bg-slate-900 min-w-[225px] mx-2 w-full max-w-[225px] rounded p-2 text-gray-400">
        {!isDropDownOpen && (
          <div onClick={openDropDown} className="flex items-center cursor-pointer">
            <div className="text-xl mr-2">
              <AiOutlinePlus />
            </div>
            <div>
              <p>Add another list</p>
            </div>
          </div>
        )}
        {isDropDownOpen && (
          <div>
            <form onSubmit={handleOnSubmit}>
              <input
                onChange={handleOnChange}
                placeholder="Enter list name..."
                className="border rounded h-9 border-gray-800 bg-transparent w-full"
              />
              {error.length > 0 && (
                <div className="my-1">
                  <p className="text-xs text-red-300">{error}</p>
                </div>
              )}
              <div className="flex items-center justify-between my-2">
                <button type="submit" className="btn">
                  Add list
                </button>
                <div onClick={closeDropDown} className="text-xl cursor-pointer">
                  <AiOutlineClose />
                </div>
              </div>
            </form>
          </div>
        )}
      </div>
    </>
  );
};

export default AddTodoList;
