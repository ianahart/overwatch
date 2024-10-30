import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateReplyCommentRequest,
  ICreateReplyCommentResponse,
  IDeleteReplyCommentRequest,
  IDeleteReplyCommentResponse,
  IGetReplyCommentsByUserRequest,
  IGetReplyCommentsByUserResponse,
  IGetReplyCommentsRequest,
  IGetReplyCommentsResponse,
  IUpdateReplyCommentRequest,
  IUpdateReplyCommentResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const replyCommentsApi = createApi({
  reducerPath: 'replyComments',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['ReplyComment'],
  endpoints(builder) {
    return {
      fetchReplyComments: builder.query<IGetReplyCommentsResponse, IGetReplyCommentsRequest>({
        query: ({ token, page, pageSize, direction, commentId }) => {
          if (!token || !commentId) {
            return '';
          }
          return {
            url: `/comments/${commentId}/reply?&page=${page}&pageSize=${pageSize}&direction=${direction}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        //@ts-ignore
        providesTags: (result, error, arg) =>
          result
            ? [
                ...result.data.items.map(({ id }) => ({ type: 'ReplyComment', id })),
                { type: 'ReplyComment', id: 'LIST' },
              ]
            : [{ type: 'ReplyComment', id: 'LIST' }],
      }),

      fetchReplyCommentsByUser: builder.query<IGetReplyCommentsByUserResponse, IGetReplyCommentsByUserRequest>({
        query: ({ userId, token, page, pageSize, direction, commentId }) => {
          if (!userId || !token || !commentId) {
            return '';
          }
          return {
            url: `/comments/${commentId}/reply/user/${userId}?&page=${page}&pageSize=${pageSize}&direction=${direction}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        //@ts-ignore
        providesTags: (result, error, arg) =>
          result
            ? [
                ...result.data.items.map(({ id }) => ({ type: 'ReplyComment', id })),
                { type: 'ReplyComment', id: 'LIST' },
              ]
            : [{ type: 'ReplyComment', id: 'LIST' }],
      }),
      updateReplyComment: builder.mutation<IUpdateReplyCommentResponse, IUpdateReplyCommentRequest>({
        query: ({ token, replyCommentId, commentId, content }) => {
          return {
            url: `/comments/${commentId}/reply/${replyCommentId}`,
            method: 'PATCH',
            body: { content },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
      createReplyComment: builder.mutation<ICreateReplyCommentResponse, ICreateReplyCommentRequest>({
        query: ({ token, userId, commentId, content }) => {
          return {
            url: `/comments/${commentId}/reply`,
            method: 'POST',
            body: { userId, content },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        invalidatesTags: [{ type: 'ReplyComment', id: 'LIST' }],
      }),
      deleteReplyComment: builder.mutation<IDeleteReplyCommentResponse, IDeleteReplyCommentRequest>({
        query: ({ commentId, replyCommentId, token }) => ({
          url: `comments/${commentId}/reply/${replyCommentId}`,
          method: 'DELETE',
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }),
        //@ts-ignore
        invalidatesTags: (_, error, { replyCommentId }) => [{ type: 'ReplyComment', id: 'LIST' }],
      }),
    };
  },
});
export const {
  useDeleteReplyCommentMutation,
  useUpdateReplyCommentMutation,
  useLazyFetchReplyCommentsQuery,
  useLazyFetchReplyCommentsByUserQuery,
  useCreateReplyCommentMutation,
} = replyCommentsApi;
export { replyCommentsApi };
