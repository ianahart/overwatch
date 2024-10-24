import { createApi } from '@reduxjs/toolkit/query/react';
import { ICreateCommentRequest, ICreateCommentResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const commentsApi = createApi({
  reducerPath: 'comments',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['Comment'],
  endpoints(builder) {
    return {
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
export const { useCreateCommentMutation } = commentsApi;
export { commentsApi };
