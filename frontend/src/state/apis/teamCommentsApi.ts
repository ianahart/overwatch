import { createApi } from '@reduxjs/toolkit/query/react';
import { ICreateTeamCommentRequest, ICreateTeamCommentResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const teamCommentsApi = createApi({
  reducerPath: 'teamComments',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['TeamComment'],
  endpoints(builder) {
    return {
      createTeamComment: builder.mutation<ICreateTeamCommentResponse, ICreateTeamCommentRequest>({
        query: ({ teamPostId, userId, content, token }) => ({
          url: `team-posts/${teamPostId}/team-comments`,
          method: 'POST',
          headers: {
            Authorization: `Bearer ${token}`,
          },
          body: { userId, content },
        }),
        //@ts-ignore
        invalidatesTags: (_, error) => [{ type: 'TeamComment', id: 'LIST' }],
      }),
    };
  },
});
export const { useCreateTeamCommentMutation } = teamCommentsApi;
export { teamCommentsApi };
