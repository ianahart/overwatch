import { createApi } from '@reduxjs/toolkit/query/react';
import {
  IGetAllTeamPostsResponse,
  IGetAllTeamPostsRequest,
  ICreateTeamPostRequest,
  ICreateTeamPostResponse,
  IDeleteTeamPostRequest,
  IDeleteTeamPostResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const teamPostsApi = createApi({
  reducerPath: 'teamPosts',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['TeamPost'],
  endpoints(builder) {
    return {
      createTeamPost: builder.mutation<ICreateTeamPostResponse, ICreateTeamPostRequest>({
        query: ({ token, teamId, code, userId, language }) => {
          return {
            url: `/teams/${teamId}/team-posts`,
            method: 'POST',
            body: { code, userId, language },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        invalidatesTags: [{ type: 'TeamPost', id: 'LIST' }],
      }),
      fetchTeamPosts: builder.query<IGetAllTeamPostsResponse, IGetAllTeamPostsRequest>({
        query: ({ teamId, token, page, pageSize, direction }) => {
          if (teamId === 0 || teamId === null || !token) {
            return '';
          }
          return {
            url: `/teams/${teamId}/team-posts?page=${page}&pageSize=${pageSize}&direction=${direction}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        //@ts-ignore
        providesTags: (result, error, arg) =>
          result
            ? [...result.data.items.map(({ id }) => ({ type: 'TeamPost', id })), { type: 'TeamPost', id: 'LIST' }]
            : [{ type: 'TeamPost', id: 'LIST' }],
      }),
      deleteTeamPost: builder.mutation<IDeleteTeamPostResponse, IDeleteTeamPostRequest>({
        query: ({ teamPostId, token }) => ({
          url: `/team-posts/${teamPostId}`,
          method: 'DELETE',
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }),
        //@ts-ignore
        invalidatesTags: (_, error, { teamPostId }) => [
          { type: 'TeamPost', id: teamPostId },
          { type: 'TeamPost', id: 'LIST' },
        ],
      }),
    };
  },
});
export const {
  useDeleteTeamPostMutation,
  useCreateTeamPostMutation,
  useLazyFetchTeamPostsQuery,
  useFetchTeamPostsQuery,
} = teamPostsApi;
export { teamPostsApi };
