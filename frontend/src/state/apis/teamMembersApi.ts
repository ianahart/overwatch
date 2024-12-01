import { createApi } from '@reduxjs/toolkit/query/react';
import {
  IDeleteTeamMemberRequest,
  IDeleteTeamMemberResponse,
  IGetAllTeamMembersRequest,
  IGetAllTeamMembersResponse,
  IGetTeamMemberTeamRequest,
  IGetTeamMemberTeamResponse,
  ISearchTeamMembersRequest,
  ISearchTeamMembersResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const teamMembersApi = createApi({
  reducerPath: 'teamMembers',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['TeamMember'],
  endpoints(builder) {
    return {
      deleteTeamMember: builder.mutation<IDeleteTeamMemberResponse, IDeleteTeamMemberRequest>({
        query: ({ teamMemberId, token }) => ({
          url: `team-members/${teamMemberId}`,
          method: 'DELETE',
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }),
        //@ts-ignore
        invalidatesTags: (_, error, { teamMemberId }) => [
          { type: 'TeamMember', id: teamMemberId },
          { type: 'TeamMember', id: 'LIST' },
        ],
      }),
      fetchTeamMembers: builder.query<IGetAllTeamMembersResponse, IGetAllTeamMembersRequest>({
        query: ({ teamId, token, page, pageSize, direction }) => {
          if (teamId === 0 || teamId === null || !token) {
            return '';
          }
          return {
            url: `/teams/${teamId}/team-members?&page=${page}&pageSize=${pageSize}&direction=${direction}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        //@ts-ignore
        providesTags: (result, error, arg) =>
          result
            ? [...result.data.items.map(({ id }) => ({ type: 'TeamMember', id })), { type: 'TeamMember', id: 'LIST' }]
            : [{ type: 'TeamMember', id: 'LIST' }],
      }),
      fetchTeamMemberTeams: builder.query<IGetTeamMemberTeamResponse, IGetTeamMemberTeamRequest>({
        query: ({ userId, token, page, pageSize, direction }) => {
          if (userId === 0 || userId === null || !token) {
            return '';
          }
          return {
            url: `/team-members/${userId}/teams?&page=${page}&pageSize=${pageSize}&direction=${direction}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        //@ts-ignore
        providesTags: (result, error, arg) =>
          result
            ? [...result.data.items.map(({ id }) => ({ type: 'TeamMember', id })), { type: 'TeamMember', id: 'LIST' }]
            : [{ type: 'TeamMember', id: 'LIST' }],
      }),
      searchTeamMembers: builder.query<ISearchTeamMembersResponse, ISearchTeamMembersRequest>({
        query: ({ teamId, token, page, pageSize, direction, search }) => {
          if (teamId === 0 || teamId === null || !token) {
            return '';
          }
          return {
            url: `/teams/${teamId}/team-members/search?search=${search}&page=${page}&pageSize=${pageSize}&direction=${direction}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        //@ts-ignore
        providesTags: (result, error, arg) =>
          result
            ? [...result.data.items.map(({ id }) => ({ type: 'TeamMember', id })), { type: 'TeamMember', id: 'LIST' }]
            : [{ type: 'TeamMember', id: 'LIST' }],
      }),
    };
  },
});
export const {
  useLazySearchTeamMembersQuery,
  useDeleteTeamMemberMutation,
  useFetchTeamMemberTeamsQuery,
  useLazyFetchTeamMemberTeamsQuery,
  useFetchTeamMembersQuery,
  useLazyFetchTeamMembersQuery,
} = teamMembersApi;
export { teamMembersApi };
