import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateTeamCommentRequest,
  ICreateTeamCommentResponse,
  IGetAllTeamCommentsRequest,
  IGetAllTeamCommentsResponse,
  IGetTeamCommentRequest,
  IGetTeamCommentResponse,
  IUpdateTeamCommentRequest,
  IUpdateTeamCommentResponse,
  IDeleteTeamCommentRequest,
  IDeleteTeamCommentResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const teamCommentsApi = createApi({
  reducerPath: 'teamComments',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['TeamComment'],
  endpoints(builder) {
    return {
      deleteTeamComment: builder.mutation<IDeleteTeamCommentResponse, IDeleteTeamCommentRequest>({
        query: ({ teamPostId, teamCommentId, token }) => {
          if (!teamPostId || !teamCommentId || !token) {
            return '';
          }
          return {
            url: `/team-posts/${teamPostId}/team-comments/${teamCommentId}`,
            method: 'DELETE',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        //@ts-ignore
        invalidatesTags: (_, error, { teamCommentId }) => {
          console.log(error);
          return [
            { type: 'TeamComment', id: teamCommentId },
            { type: 'TeamComment', id: 'LIST' },
          ];
        },
      }),
      updateTeamComment: builder.mutation<IUpdateTeamCommentResponse, IUpdateTeamCommentRequest>({
        query: ({ teamPostId, content, teamCommentId, token, tag }) => {
          console.log(content);
          if (!teamPostId || !teamCommentId || !token) {
            return '';
          }
          return {
            url: `/team-posts/${teamPostId}/team-comments/${teamCommentId}`,
            method: 'PATCH',
            headers: {
              Authorization: `Bearer ${token}`,
            },
            body: { content, tag },
          };
        },
        //@ts-ignore
        invalidatesTags: (_, error, { teamCommentId }) => {
          console.log(error);
          return [
            { type: 'TeamComment', id: teamCommentId },
            { type: 'TeamComment', id: 'LIST' },
          ];
        },
      }),

      fetchTeamComment: builder.query<IGetTeamCommentResponse, IGetTeamCommentRequest>({
        query: ({ teamPostId, teamCommentId, token }) => {
          if (!teamPostId || !teamCommentId || !token) {
            return '';
          }
          return {
            url: `/team-posts/${teamPostId}/team-comments/${teamCommentId}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
      createTeamComment: builder.mutation<ICreateTeamCommentResponse, ICreateTeamCommentRequest>({
        query: ({ teamPostId, userId, tag, content, token }) => ({
          url: `team-posts/${teamPostId}/team-comments`,
          method: 'POST',
          headers: {
            Authorization: `Bearer ${token}`,
          },
          body: { userId, content, tag },
        }),
        //@ts-ignore
        invalidatesTags: (_, error) => [{ type: 'TeamComment', id: 'LIST' }],
      }),
      fetchTeamComments: builder.query<IGetAllTeamCommentsResponse, IGetAllTeamCommentsRequest>({
        query: ({ teamPostId, token, page, pageSize, direction }) => {
          if (teamPostId === 0 || teamPostId === null || !token) {
            return '';
          }
          return {
            url: `/team-posts/${teamPostId}/team-comments?&page=${page}&pageSize=${pageSize}&direction=${direction}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        //@ts-ignore
        providesTags: (result, error, arg) =>
          result
            ? [...result.data.items.map(({ id }) => ({ type: 'TeamComment', id })), { type: 'TeamComment', id: 'LIST' }]
            : [{ type: 'TeamComment', id: 'LIST' }],
      }),
    };
  },
});
export const {
  useFetchTeamCommentQuery,
  useUpdateTeamCommentMutation,
  useDeleteTeamCommentMutation,
  useCreateTeamCommentMutation,
  useFetchTeamCommentsQuery,
  useLazyFetchTeamCommentsQuery,
} = teamCommentsApi;
export { teamCommentsApi };
