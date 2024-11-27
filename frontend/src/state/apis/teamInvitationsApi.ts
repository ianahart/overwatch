import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateTeamInvitationRequest,
  ICreateTeamInvitationResponse,
  IGetAllTeamInvitationsRequest,
  IGetAllTeamInvitationsResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const teamInvitationsApi = createApi({
  reducerPath: 'teamInvitations',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['TeamInvitation'],
  endpoints(builder) {
    return {
      fetchTeamInvitations: builder.query<IGetAllTeamInvitationsResponse, IGetAllTeamInvitationsRequest>({
        query: ({ userId, token, page, pageSize, direction }) => {
          if (userId === 0 || userId === null) {
            return '';
          }
          return {
            url: `/teams/invitations?userId=${userId}&page=${page}&pageSize=${pageSize}&direction=${direction}`,
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
                ...result.data.items.map(({ id }) => ({ type: 'TeamInvitation', id })),
                { type: 'TeamInvitation', id: 'LIST' },
              ]
            : [{ type: 'TeamInvitation', id: 'LIST' }],
      }),

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
export const { useCreateTeamInvitationMutation, useFetchTeamInvitationsQuery, useLazyFetchTeamInvitationsQuery } =
  teamInvitationsApi;
export { teamInvitationsApi };
