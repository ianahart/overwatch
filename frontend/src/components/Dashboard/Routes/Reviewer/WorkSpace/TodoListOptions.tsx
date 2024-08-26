import { BsTrash } from 'react-icons/bs';
import { useDispatch, useSelector } from 'react-redux';
import { ToastContainer, toast } from 'react-toastify';

import {
  TRootState,
  deleteSingleTodoList,
  setTodoLists,
  useDeleteTodoListMutation,
  useReorderTodoListsMutation,
} from '../../../../../state/store';
import 'react-toastify/dist/ReactToastify.css';
import { ITodoList } from '../../../../../interfaces';

export interface ITodoListOptionsProps {
  todoListId: number;
  onClickClose: () => void;
}

const TodoListOptions = ({ todoListId, onClickClose }: ITodoListOptionsProps) => {
  const dispatch = useDispatch();
  const { token } = useSelector((store: TRootState) => store.user);
  const { todoLists } = useSelector((store: TRootState) => store.todoList);
  const { workSpace } = useSelector((store: TRootState) => store.workSpace);
  const [deleteTodoList] = useDeleteTodoListMutation();
  const [reorderTodoLists] = useReorderTodoListsMutation();

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

  const handleReorderTodoLists = (newTodoLists: ITodoList[], workSpaceId: number) => {
    reorderTodoLists({ token, todoLists: newTodoLists, workSpaceId })
      .unwrap()
      .then((res) => {
        dispatch(setTodoLists(res.data));
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const handleOnDelete = (e: React.MouseEvent<HTMLLIElement>) => {
    e.stopPropagation();
    deleteTodoList({ token, id: todoListId })
      .unwrap()
      .then(() => {
        onClickClose();
        initiateToast();
        dispatch(deleteSingleTodoList(todoListId));
      })
      .then(() => {
        handleReorderTodoLists(todoLists, workSpace.id);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div className=" absolute top-4 right-0 bg-gray-950 rounded w-full max-w-[125px] min-h-[90px] min-w-[125px]">
      <ul>
        <li onClick={handleOnDelete} className="flex items-center p-2 border-b border-b-gray-800 pointer-events-auto">
          <BsTrash className="mr-1" />
          <button className="text-xs">Remove</button>
        </li>
      </ul>
      <ToastContainer />
    </div>
  );
};

export default TodoListOptions;
