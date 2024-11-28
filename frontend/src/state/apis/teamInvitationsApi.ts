import { createApi } from '@reduxjs/toolkit/query/react';
import {
  IUpdateTeamInvitationRequest,
  IDeleteTeamInvitationRequest,
  IDeleteTeamInvitationResponse,
  ICreateTeamInvitationRequest,
  ICreateTeamInvitationResponse,
  IGetAllTeamInvitationsRequest,
  IGetAllTeamInvitationsResponse,
  IUpdateTeamInvitationResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const teamInvitationsApi = createApi({
  reducerPath: 'teamInvitations',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['TeamInvitation', 'TeamMember'],
  endpoints(builder) {
    return {
      updateTeamInvitation: builder.mutation<IUpdateTeamInvitationResponse, IUpdateTeamInvitationRequest>({
        query: ({ teamInvitationId, teamId, userId, token }) => ({
          url: `teams/${teamId}/invitations/${teamInvitationId}`,
          method: 'PATCH',
          body: { userId },
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }),
        //@ts-ignore
        invalidatesTags: (_, error, { teamInvitationId, teamId }) => [
          { type: 'TeamInvitation', id: teamInvitationId },
          { type: 'TeamInvitation', id: 'LIST' },
          { type: 'TeamMember', id: 'LIST' },
        ],
      }),

      deleteTeamInvitation: builder.mutation<IDeleteTeamInvitationResponse, IDeleteTeamInvitationRequest>({
        query: ({ teamInvitationId, token }) => ({
          url: `teams/invitations/${teamInvitationId}`,
          method: 'DELETE',
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }),
        //@ts-ignore
        invalidatesTags: (_, error, { teamInvitationId }) => [
          { type: 'TeamInvitation', id: teamInvitationId },
          { type: 'TeamInvitation', id: 'LIST' },
        ],
      }),

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
export const {
  useUpdateTeamInvitationMutation,
  useDeleteTeamInvitationMutation,
  useCreateTeamInvitationMutation,
  useFetchTeamInvitationsQuery,
  useLazyFetchTeamInvitationsQuery,
} = teamInvitationsApi;
export { teamInvitationsApi };
