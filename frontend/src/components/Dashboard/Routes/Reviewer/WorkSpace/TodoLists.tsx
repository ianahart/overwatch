import { useDispatch, useSelector } from 'react-redux';
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { FetchBaseQueryError } from '@reduxjs/toolkit/dist/query';
import { SerializedError } from '@reduxjs/toolkit';
import { SortableContext, horizontalListSortingStrategy, verticalListSortingStrategy } from '@dnd-kit/sortable';

export type TServerError = FetchBaseQueryError | SerializedError;

import { TRootState, setTodoLists, useFetchTodoListsQuery } from '../../../../../state/store';
import Spinner from '../../../../Shared/Spinner';
import TodoList from './TodoList';
import AddTodoList from './AddTodoList';
import { retrieveTokens } from '../../../../../util';

const TodoLists = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { workSpace } = useSelector((store: TRootState) => store.workSpace);
  const { todoLists: lists } = useSelector((store: TRootState) => store.todoList);
  const {
    data,
    isLoading,
    isError,
    error: serverError,
  } = useFetchTodoListsQuery({
    token: retrieveTokens().token,
    workSpaceId: workSpace.id,
  });

  const showErrorPage = (error: TServerError) => {
    navigate('*', { state: { ...error } });
  };

  useEffect(() => {
    if (data !== undefined) {
      dispatch(setTodoLists(data.data));
    }

    if (isError) {
      showErrorPage(serverError);
    }
  }, [data, dispatch, isError]);

  return (
    <div>
      {isLoading && <Spinner message="Loading workspace..." />}
      <div className="flex items-center justify-evenly">
        <SortableContext items={lists} strategy={horizontalListSortingStrategy}>
          {lists.map((list) => {
            return <TodoList key={list.id} list={list} />;
          })}
        </SortableContext>
        <AddTodoList />
      </div>
    </div>
  );
};

export default TodoLists;
