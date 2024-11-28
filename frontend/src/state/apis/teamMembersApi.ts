import { createApi } from '@reduxjs/toolkit/query/react';
import { IGetTeamMemberTeamRequest, IGetTeamMemberTeamResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const teamMembersApi = createApi({
  reducerPath: 'teamMembers',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['TeamMember'],
  endpoints(builder) {
    return {
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
    };
  },
});
export const { useFetchTeamMemberTeamsQuery, useLazyFetchTeamMemberTeamsQuery } = teamMembersApi;
export { teamMembersApi };
