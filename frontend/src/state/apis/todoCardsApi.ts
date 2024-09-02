import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateTodoCardRequest,
  ICreateTodoCardResponse,
  IUpdateTodoCardRequest,
  IUpdateTodoCardResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const todoCardsApi = createApi({
  reducerPath: 'todoCards',
  tagTypes: ['TodoCard'],
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      updateTodoCard: builder.mutation<IUpdateTodoCardResponse, IUpdateTodoCardRequest>({
        query: ({ card, token }) => {
          return {
            url: `/todo-cards/${card.id}`,
            method: 'PUT',
            headers: {
              Authorization: `Bearer ${token}`,
            },
            body: card,
          };
        },
      }),
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

export const { useCreateTodoCardMutation, useUpdateTodoCardMutation } = todoCardsApi;
export { todoCardsApi };
