import { createApi } from '@reduxjs/toolkit/query/react';
import { ICreateSaveCommentRequest, ICreateSaveCommentResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const saveCommentsApi = createApi({
  reducerPath: 'saveComments',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['SaveComment'],
  endpoints(builder) {
    return {
      createSaveComment: builder.mutation<ICreateSaveCommentResponse, ICreateSaveCommentRequest>({
        query: ({ token, userId, commentId }) => {
          return {
            url: '/save-comments',
            method: 'POST',
            body: { userId, commentId },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        invalidatesTags: [{ type: 'SaveComment', id: 'LIST' }],
      }),
    };
  },
});
export const { useCreateSaveCommentMutation } = saveCommentsApi;
export { saveCommentsApi };
