import { createApi } from '@reduxjs/toolkit/query/react';
import { ICreateVoteRequest, ICreateVoteResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const commentVotesApi = createApi({
  reducerPath: 'commentVotes',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['CommentVote'],
  endpoints(builder) {
    return {
      createVote: builder.mutation<ICreateVoteResponse, ICreateVoteRequest>({
        query: ({ token, userId, commentId, voteType }) => {
          return {
            url: `/comments/${commentId}/votes`,
            method: 'POST',
            body: { userId, commentId, voteType },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        invalidatesTags: [{ type: 'CommentVote', id: 'LIST' }],
      }),
    };
  },
});
export const { useCreateVoteMutation } = commentVotesApi;
export { commentVotesApi };
