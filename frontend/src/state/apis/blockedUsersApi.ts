import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateBlockedUserRequest,
  ICreateBlockedUserResponse,
  IDeleteBlockedUserRequest,
  IDeleteBlockedUserResponse,
  IFetchBlockedUsersRequest,
  IFetchBlockedUsersResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const blockedUsersApi = createApi({
  reducerPath: 'blockedUsers',
  tagTypes: ['BlockedUser'],
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      createBlockedUser: builder.mutation<ICreateBlockedUserResponse, ICreateBlockedUserRequest>({
        query: ({ blockedUserId, blockerUserId, token }) => {
          return {
            url: `/block-users`,
            method: 'POST',
            headers: {
              Authorization: `Bearer ${token}`,
            },
            body: {
              blockedUserId,
              blockerUserId,
            },
          };
        },
        invalidatesTags: (result, error) => {
          console.log(result, error);
          return [{ type: 'BlockedUser', id: 'LIST' }];
        },
      }),
      fetchBlockedUsers: builder.query<IFetchBlockedUsersResponse, IFetchBlockedUsersRequest>({
        query: ({ token, blockerUserId, page, pageSize, direction }) => {
          if (blockerUserId === 0 || token === '' || token === null || token === undefined) {
            return '';
          }
          return {
            url: `/block-users?blockerUserId=${blockerUserId}&page=${page}&pageSize=${pageSize}&direction=${direction}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        //@ts-ignore
        providesTags: (result, error, arg) =>
          result
            ? [...result.data.items.map(({ id }) => ({ type: 'BlockedUser', id })), { type: 'BlockedUser', id: 'LIST' }]
            : [{ type: 'BlockedUser', id: 'LIST' }],
      }),
      deleteBlockedUser: builder.mutation<IDeleteBlockedUserResponse, IDeleteBlockedUserRequest>({
        query: ({ blockUserId, token }) => ({
          url: `block-users/${blockUserId}`,
          method: 'DELETE',
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }),
        //@ts-ignore
        invalidatesTags: (_, error, { blockUserId }) => [
          { type: 'lockedUser', id: blockUserId },
          { type: 'BlockedUser', id: 'LIST' },
        ],
      }),
    };
  },
});

export const {
  useCreateBlockedUserMutation,
  useFetchBlockedUsersQuery,
  useLazyFetchBlockedUsersQuery,
  useDeleteBlockedUserMutation,
} = blockedUsersApi;
export { blockedUsersApi };
