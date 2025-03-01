import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateTeamPinnedMessageRequest,
  ICreateTeamPinnedMessageResponse,
  IGetAllTeamPinnedMessageRequest,
  IGetAllTeamPinnedMessageResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const teamPinnedMessagesApi = createApi({
  reducerPath: 'teamPinnedMessages',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['TeamPinnedMessage'],
  endpoints(builder) {
    return {
      fetchTeamPinnedMessages: builder.query<IGetAllTeamPinnedMessageResponse, IGetAllTeamPinnedMessageRequest>({
        query: ({ token, teamId }) => {
          return {
            url: `/teams/${teamId}/team-pinned-messages`,
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
                ...result.data.map(({ id }) => ({ type: 'TeamPinnedMessage', id })),
                { type: 'TeamPinnedMessage', id: 'LIST' },
              ]
            : [{ type: 'TeamPinnedMessage', id: 'LIST' }],
      }),

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
export const { useCreateTeamPinnedMessageMutation, useFetchTeamPinnedMessagesQuery } = teamPinnedMessagesApi;
export { teamPinnedMessagesApi };
