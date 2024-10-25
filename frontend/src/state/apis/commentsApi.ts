import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateCommentRequest,
  ICreateCommentResponse,
  IGetCommentsRequest,
  IGetCommentsResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const commentsApi = createApi({
  reducerPath: 'comments',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['Comment'],
  endpoints(builder) {
    return {
      fetchComments: builder.query<IGetCommentsResponse, IGetCommentsRequest>({
        query: ({ topicId, page, pageSize, direction, sort, token = '' }) => {
          if (topicId === undefined || !topicId) {
            return '';
          }
          return {
            url: `/topics/${topicId}/comments?page=${page}&pageSize=${pageSize}&direction=${direction}&sort=${sort}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        //@ts-ignore
        providesTags: (result, error, arg) =>
          result
            ? [...result.data.items.map(({ id }) => ({ type: 'Comment', id })), { type: 'Comment', id: 'LIST' }]
            : [{ type: 'Comment', id: 'LIST' }],
      }),

      createComment: builder.mutation<ICreateCommentResponse, ICreateCommentRequest>({
        query: ({ token, userId, topicId, content }) => {
          return {
            url: '/comments',
            method: 'POST',
            body: { userId, topicId, content },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        invalidatesTags: [{ type: 'Comment', id: 'LIST' }],
      }),
    };
  },
});
export const { useCreateCommentMutation, useFetchCommentsQuery, useLazyFetchCommentsQuery } = commentsApi;
export { commentsApi };
