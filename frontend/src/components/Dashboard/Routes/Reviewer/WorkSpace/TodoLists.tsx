import { useDispatch, useSelector } from 'react-redux';
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import { TRootState, setTodoLists, useLazyFetchTodoListsQuery } from '../../../../../state/store';
import { IServerError } from '../../../../../interfaces';
import Spinner from '../../../../Shared/Spinner';
import TodoList from './TodoList';
import AddTodoList from './AddTodoList';

const TodoLists = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { token } = useSelector((store: TRootState) => store.user);
  const { workSpace } = useSelector((store: TRootState) => store.workSpace);
  const { todoLists: lists } = useSelector((store: TRootState) => store.todoList);
  const [fetchTodoLists, { isLoading }] = useLazyFetchTodoListsQuery();

  const showErrorPage = (error: IServerError) => {
    navigate('*', { state: { ...error } });
  };

  useEffect(() => {
    if (workSpace.id !== 0) {
      fetchTodoLists({ token, workSpaceId: workSpace.id })
        .unwrap()
        .then((res) => {
          dispatch(setTodoLists(res.data));
        })
        .catch((err) => {
          showErrorPage({ status: err.status, data: err.data });
        });
    }
  }, [workSpace.id]);

  return (
    <div>
      {isLoading && <Spinner message="Loading workspace..." />}
      <div className="flex items-center justify-evenly">
        {lists.map((list) => {
          return <TodoList key={list.id} list={list} />;
        })}
        <AddTodoList />
      </div>
    </div>
  );
};

export default TodoLists;
