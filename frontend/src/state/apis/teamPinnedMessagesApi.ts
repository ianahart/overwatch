import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateTeamPinnedMessageRequest,
  ICreateTeamPinnedMessageResponse,
  IDeleteTeamPinnedMessageRequest,
  IDeleteTeamPinnedMessageResponse,
  IGetAllTeamPinnedMessageRequest,
  IGetAllTeamPinnedMessageResponse,
  IReorderTeamPinnedMessageRequest,
  IReorderTeamPinnedMessageResponse,
  IUpdateTeamPinnedMessageRequest,
  IUpdateTeamPinnedMessageResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const teamPinnedMessagesApi = createApi({
  reducerPath: 'teamPinnedMessages',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['TeamPinnedMessage'],
  endpoints(builder) {
    return {
      updateTeamPinnedMessage: builder.mutation<IUpdateTeamPinnedMessageResponse, IUpdateTeamPinnedMessageRequest>({
        query: ({ teamId, token, userId, message, teamPinnedMessageId }) => {
          return {
            url: `/teams/${teamId}/team-pinned-messages/${teamPinnedMessageId}`,
            method: 'PATCH',
            body: { userId, message },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        invalidatesTags: (_, error, { teamPinnedMessageId }) => {
          console.log(error);
          return [
            { type: 'TeamPinnedMessage', id: teamPinnedMessageId },
            { type: 'TeamPinnedMessage', id: 'LIST' },
          ];
        },
      }),

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

      deleteTeamPinnedMessage: builder.mutation<IDeleteTeamPinnedMessageResponse, IDeleteTeamPinnedMessageRequest>({
        query: ({ teamPinnedMessageId, token, teamId }) => ({
          url: `teams/${teamId}/team-pinned-messages/${teamPinnedMessageId}`,
          method: 'DELETE',
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }),
        //@ts-ignore
        invalidatesTags: (_, error, { teamPinnedMessageId }) => [
          { type: 'TeamPinnedMessage', id: teamPinnedMessageId },
          { type: 'TeamPinnedMessage', id: 'LIST' },
        ],
      }),
      reorderTeamPinnedMessages: builder.mutation<IReorderTeamPinnedMessageResponse, IReorderTeamPinnedMessageRequest>({
        query: ({ teamId, token, teamPinnedMessages }) => {
          return {
            url: `/teams/${teamId}/team-pinned-messages/reorder`,
            method: 'POST',
            body: { teamPinnedMessages },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
    };
  },
});
export const {
  useReorderTeamPinnedMessagesMutation,
  useDeleteTeamPinnedMessageMutation,
  useCreateTeamPinnedMessageMutation,
  useFetchTeamPinnedMessagesQuery,
  useUpdateTeamPinnedMessageMutation,
} = teamPinnedMessagesApi;
export { teamPinnedMessagesApi };
