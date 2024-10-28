import { createApi } from '@reduxjs/toolkit/query/react';
import { ICreateReplyCommentRequest, ICreateReplyCommentResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const replyCommentsApi = createApi({
  reducerPath: 'replyComments',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['ReplyComment'],
  endpoints(builder) {
    return {
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
export const { useCreateReplyCommentMutation } = replyCommentsApi;
export { replyCommentsApi };
