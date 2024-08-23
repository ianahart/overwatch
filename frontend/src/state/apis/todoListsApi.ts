import { createApi } from '@reduxjs/toolkit/query/react';
import { ICreateTodoListResponse, ICreateTodoListRequest } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const todoListsApi = createApi({
  reducerPath: 'todoLists',
  tagTypes: ['TodoList'],
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
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

export const { useCreateTodoListMutation } = todoListsApi;
export { todoListsApi };
