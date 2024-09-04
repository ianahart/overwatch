import { createApi } from '@reduxjs/toolkit/query/react';
import {
  IReorderTodoCardRequest,
  IReorderTodoCardResponse,
  ICreateTodoCardRequest,
  ICreateTodoCardResponse,
  IDeleteTodoCardRequest,
  IDeleteTodoCardResponse,
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
      reorderTodoCards: builder.mutation<IReorderTodoCardResponse, IReorderTodoCardRequest>({
        query: ({ token, listId, oldIndex, newIndex, todoCardId }) => {
          return {
            url: `/todo-cards/${todoCardId}/reorder`,
            method: 'PATCH',
            headers: {
              Authorization: `Bearer ${token}`,
            },
            body: {
              todoListId: listId,
              oldIndex,
              newIndex,
            },
          };
        },
      }),

      deleteTodoCard: builder.mutation<IDeleteTodoCardResponse, IDeleteTodoCardRequest>({
        query: ({ token, todoCardId }) => {
          return {
            url: `/todo-cards/${todoCardId}`,
            method: 'DELETE',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
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

export const {
  useReorderTodoCardsMutation,
  useDeleteTodoCardMutation,
  useCreateTodoCardMutation,
  useUpdateTodoCardMutation,
} = todoCardsApi;
export { todoCardsApi };
