import { useDispatch, useSelector } from 'react-redux';
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { FetchBaseQueryError } from '@reduxjs/toolkit/dist/query';
import { SerializedError } from '@reduxjs/toolkit';
import { SortableContext, verticalListSortingStrategy } from '@dnd-kit/sortable';

export type TServerError = FetchBaseQueryError | SerializedError;

import { TRootState, setTodoLists, useFetchTodoListsQuery } from '../../../../../state/store';
import Spinner from '../../../../Shared/Spinner';
import TodoList from './TodoList';
import AddTodoList from './AddTodoList';
import { retrieveTokens } from '../../../../../util';

const TodoLists = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  //@ts-ignore
  const { workSpace } = useSelector((store: TRootState) => store.workSpace);
  //@ts-ignore
  const { todoLists: lists } = useSelector((store: TRootState) => store.todoList);
  const {
    data,
    isLoading,
    isError,
    error: serverError,
  } = useFetchTodoListsQuery(
    {
      token: retrieveTokens().token,
      workSpaceId: workSpace.id,
    },
    { skip: !retrieveTokens().token || !workSpace.id }
  );

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
        {lists.map((list) => (
          <SortableContext
            key={`list-context-${list.id}`}
            id={`list-${list.id}`}
            items={lists.map((list) => `list-${list.id}`)}
            strategy={verticalListSortingStrategy}
          >
            <TodoList list={list} />
          </SortableContext>
        ))}
        <AddTodoList />
      </div>
    </div>
  );
};

export default TodoLists;
