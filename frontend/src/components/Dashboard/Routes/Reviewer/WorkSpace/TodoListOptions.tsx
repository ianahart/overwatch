import { BsTrash } from 'react-icons/bs';
import { useSelector } from 'react-redux';
import { ToastContainer, toast } from 'react-toastify';

import { TRootState, useDeleteTodoListMutation } from '../../../../../state/store';
import 'react-toastify/dist/ReactToastify.css';

export interface ITodoListOptionsProps {
  todoListId: number;
  onClickClose: () => void;
}

const TodoListOptions = ({ todoListId, onClickClose }: ITodoListOptionsProps) => {
  const { token } = useSelector((store: TRootState) => store.user);
  const [deleteTodoList] = useDeleteTodoListMutation();

  const initiateToast = () => {
    toast.success('Your todo list was deleted.', {
      position: 'bottom-center',
      autoClose: 5000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
      theme: 'dark',
    });
  };

  const handleOnDelete = () => {
    deleteTodoList({ token, id: todoListId })
      .unwrap()
      .then(() => {
        onClickClose();
        initiateToast();
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div className=" absolute top-4 right-0 bg-gray-950 rounded w-full max-w-[125px] min-h-[90px] min-w-[125px]">
      <ul>
        <li onClick={handleOnDelete} className="flex items-center p-2 border-b border-b-gray-800 cursor-pointer">
          <BsTrash className="mr-1" />
          <button className="text-xs">Remove</button>
        </li>
      </ul>
      <ToastContainer />
    </div>
  );
};

export default TodoListOptions;
