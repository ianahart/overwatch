import { createApi } from '@reduxjs/toolkit/query/react';
import { ICreateTeamRequest, ICreateTeamResponse, IGetAllTeamsRequest, IGetAllTeamsResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const teamsApi = createApi({
  reducerPath: 'teams',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['Team'],
  endpoints(builder) {
    return {
      fetchTeams: builder.query<IGetAllTeamsResponse, IGetAllTeamsRequest>({
        query: ({ userId, token, page, pageSize, direction }) => {
          if (userId === 0 || userId === null || !token) {
            return '';
          }
          return {
            url: `/teams?userId=${userId}&page=${page}&pageSize=${pageSize}&direction=${direction}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        //@ts-ignore
        providesTags: (result, error, arg) =>
          result
            ? [...result.data.items.map(({ id }) => ({ type: 'Team', id })), { type: 'Team', id: 'LIST' }]
            : [{ type: 'Team', id: 'LIST' }],
      }),

      createTeam: builder.mutation<ICreateTeamResponse, ICreateTeamRequest>({
        query: ({ userId, teamName, token, teamDescription }) => {
          return {
            url: `/teams`,
            method: 'POST',
            body: { userId, teamName, teamDescription },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        invalidatesTags: [{ type: 'Team', id: 'LIST' }],
      }),
    };
  },
});
export const { useFetchTeamsQuery, useCreateTeamMutation, useLazyFetchTeamsQuery } = teamsApi;
export { teamsApi };
