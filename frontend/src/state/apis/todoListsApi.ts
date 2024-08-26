import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateTodoListResponse,
  ICreateTodoListRequest,
  IFetchTodoListsResponse,
  IFetchTodoListsRequest,
  IUpdateTodoListRequest,
  IUpdateTodoListResponse,
  IDeleteTodoListRequest,
  IDeleteTodoListResponse,
  IReorderTodoListResponse,
  IReorderTodoListRequest,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const todoListsApi = createApi({
  reducerPath: 'todoLists',
  tagTypes: ['TodoList'],
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      reorderTodoLists: builder.mutation<IReorderTodoListResponse, IReorderTodoListRequest>({
        query: ({ token, todoLists, workSpaceId }) => {
          return {
            url: `/workspaces/${workSpaceId}/todo-lists/reorder`,
            method: 'POST',
            body: {
              todoLists,
            },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
      deleteTodoList: builder.mutation<IDeleteTodoListResponse, IDeleteTodoListRequest>({
        query: ({ id, token }) => ({
          url: `todo-lists/${id}`,
          method: 'DELETE',
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }),
      }),
      editTodoList: builder.mutation<IUpdateTodoListResponse, IUpdateTodoListRequest>({
        query: ({ token, id, title, index, workSpaceId }) => {
          return {
            url: `/todo-lists/${id}`,
            method: 'PATCH',
            body: {
              title,
              index,
              workSpaceId,
            },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        invalidatesTags: (_, error, { id }) => {
          console.log(error);
          return [
            { type: 'TodoList', id: id },
            { type: 'TodoList', id: 'LIST' },
          ];
        },
      }),
      fetchTodoLists: builder.query<IFetchTodoListsResponse, IFetchTodoListsRequest>({
        query: ({ token, workSpaceId }) => {
          if (workSpaceId === 0 || workSpaceId === null) {
            return '';
          }
          return {
            url: `/workspaces/${workSpaceId}/todo-lists`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        //@ts-ignore
        providesTags: (result, error, arg) =>
          result
            ? [...result.data.map(({ id }) => ({ type: 'TodoList', id })), { type: 'TodoList', id: 'LIST' }]
            : [{ type: 'TodoList', id: 'LIST' }],
      }),

      createTodoList: builder.mutation<ICreateTodoListResponse, ICreateTodoListRequest>({
        query: ({ userId, token, title, workSpaceId, index }) => {
          return {
            url: `/workspaces/${workSpaceId}/todo-lists`,
            method: 'POST',
            headers: {
              Authorization: `Bearer ${token}`,
            },
            body: {
              title,
              userId,
              index,
            },
          };
        },
      }),
    };
  },
});

export const {
  useReorderTodoListsMutation,
  useFetchTodoListsQuery,
  useDeleteTodoListMutation,
  useCreateTodoListMutation,
  useLazyFetchTodoListsQuery,
  useEditTodoListMutation,
} = todoListsApi;
export { todoListsApi };
