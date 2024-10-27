import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateReactionResponse,
  ICreateReactionRequest,
  IGetReactionResponse,
  IGetReactionRequest,
  IDeleteReactionResponse,
  IDeleteReactionRequest,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const reactionsApi = createApi({
  reducerPath: 'reactions',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['Reaction'],
  endpoints(builder) {
    return {
      fetchReaction: builder.query<IGetReactionResponse, IGetReactionRequest>({
        query: ({ token, userId, commentId }) => {
          if (!token || !userId || !commentId) {
            return '';
          }
          return {
            url: `/comments/${commentId}/reactions?userId=${userId}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
      deleteReaction: builder.mutation<IDeleteReactionResponse, IDeleteReactionRequest>({
        query: ({ token, userId, commentId }) => {
          return {
            url: `/comments/${commentId}/reactions/${userId}`,
            method: 'DELETE',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
      createReaction: builder.mutation<ICreateReactionResponse, ICreateReactionRequest>({
        query: ({ token, userId, commentId, emoji }) => {
          return {
            url: `/comments/${commentId}/reactions`,
            method: 'POST',
            body: { userId, emoji },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        invalidatesTags: [{ type: 'Reaction', id: 'LIST' }],
      }),
    };
  },
});
export const { useCreateReactionMutation, useFetchReactionQuery, useDeleteReactionMutation } = reactionsApi;
export { reactionsApi };
