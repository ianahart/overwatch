import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateReplyCommentRequest,
  ICreateReplyCommentResponse,
  IGetReplyCommentsByUserRequest,
  IGetReplyCommentsByUserResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const replyCommentsApi = createApi({
  reducerPath: 'replyComments',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['ReplyComment'],
  endpoints(builder) {
    return {
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
    };
  },
});
export const { useLazyFetchReplyCommentsByUserQuery, useCreateReplyCommentMutation } = replyCommentsApi;
export { replyCommentsApi };
