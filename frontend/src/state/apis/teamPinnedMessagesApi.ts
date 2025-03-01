import { createApi } from '@reduxjs/toolkit/query/react';
import { ICreateTeamPinnedMessageRequest, ICreateTeamPinnedMessageResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const teamPinnedMessagesApi = createApi({
  reducerPath: 'teamPinnedMessages',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['TeamPinnedMessage'],
  endpoints(builder) {
    return {
      createTeamPinnedMessage: builder.mutation<ICreateTeamPinnedMessageResponse, ICreateTeamPinnedMessageRequest>({
        query: ({ token, teamId, message, userId }) => {
          return {
            url: `/teams/${teamId}/team-pinned-messages`,
            method: 'POST',
            body: { message, userId },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        invalidatesTags: [{ type: 'TeamPinnedMessage', id: 'LIST' }],
      }),
    };
  },
});
export const { useCreateTeamPinnedMessageMutation } = teamPinnedMessagesApi;
export { teamPinnedMessagesApi };
