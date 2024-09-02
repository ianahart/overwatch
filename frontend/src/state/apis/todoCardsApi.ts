import { createApi } from '@reduxjs/toolkit/query/react';
import { ICreateTodoCardRequest, ICreateTodoCardResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const todoCardsApi = createApi({
  reducerPath: 'todoCards',
  tagTypes: ['TodoCard'],
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      createTodoCard: builder.mutation<ICreateTodoCardResponse, ICreateTodoCardRequest>({
        query: ({ userId, token, title, todoListId, index }) => {
          return {
            url: `/todo-lists/${todoListId}/todo-cards`,
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

export const { useCreateTodoCardMutation } = todoCardsApi;
export { todoCardsApi };
