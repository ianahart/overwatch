import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateTeamCommentRequest,
  ICreateTeamCommentResponse,
  IGetAllTeamCommentsRequest,
  IGetAllTeamCommentsResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const teamCommentsApi = createApi({
  reducerPath: 'teamComments',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['TeamComment'],
  endpoints(builder) {
    return {
      createTeamComment: builder.mutation<ICreateTeamCommentResponse, ICreateTeamCommentRequest>({
        query: ({ teamPostId, userId, content, token }) => ({
          url: `team-posts/${teamPostId}/team-comments`,
          method: 'POST',
          headers: {
            Authorization: `Bearer ${token}`,
          },
          body: { userId, content },
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
export const { useCreateTeamCommentMutation, useFetchTeamCommentsQuery, useLazyFetchTeamCommentsQuery } =
  teamCommentsApi;
export { teamCommentsApi };
