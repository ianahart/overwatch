import { createApi } from '@reduxjs/toolkit/query/react';
import { ICreateTeamInvitationRequest, ICreateTeamInvitationResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const teamInvitationsApi = createApi({
  reducerPath: 'teamInvitations',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['TeamInvitation'],
  endpoints(builder) {
    return {
      createTeamInvitation: builder.mutation<ICreateTeamInvitationResponse, ICreateTeamInvitationRequest>({
        query: ({ senderId, receiverId, teamId, token }) => {
          return {
            url: `/teams/${teamId}/invitations`,
            method: 'POST',
            body: { senderId, receiverId },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        invalidatesTags: [{ type: 'TeamInvitation', id: 'LIST' }],
      }),
    };
  },
});
export const { useCreateTeamInvitationMutation } = teamInvitationsApi;
export { teamInvitationsApi };
