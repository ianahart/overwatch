import { createApi } from '@reduxjs/toolkit/query/react';
import { IGetAllTeamMessagesRequest, IGetAllTeamMessagesResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const teamMessagesApi = createApi({
  reducerPath: 'teamMessages',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['TeamMessage'],
  endpoints(builder) {
    return {
      fetchTeamMessages: builder.query<IGetAllTeamMessagesResponse, IGetAllTeamMessagesRequest>({
        query: ({ currentTeam, token }) => {
          if (currentTeam === 0 || currentTeam === null || !token) {
            return '';
          }
          return {
            url: `/teams/${currentTeam}/team-messages`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
    };
  },
});
export const { useLazyFetchTeamMessagesQuery } = teamMessagesApi;
export { teamMessagesApi };
