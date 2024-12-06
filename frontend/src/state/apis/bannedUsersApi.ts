import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateBannedUserRequest,
  ICreateBannedUserResponse,
  IDeleteBanRequest,
  IDeleteBanResponse,
  IGetAllBanRequest,
  IGetAllBanResponse,
  IGetBanRequest,
  IGetBanResponse,
  IUpdateBanRequest,
  IUpdateBanResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const bannedUsersApi = createApi({
  reducerPath: 'bannedUsers',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['BannedUser'],
  endpoints(builder) {
    return {
      DeleteBannedUser: builder.mutation<IDeleteBanResponse, IDeleteBanRequest>({
        query: ({ banId, token }) => {
          if (banId === 0 || banId === undefined || !token) {
            return '';
          }
          return {
            url: `/admin/banned-users/${banId}`,
            method: 'DELETE',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        invalidatesTags: (_, error, { banId }) => {
          console.log(error);
          return [
            { type: 'BannedUser', id: banId },
            { type: 'BannedUser', id: 'LIST' },
          ];
        },
      }),
      fetchBannedUser: builder.query<IGetBanResponse, IGetBanRequest>({
        query: ({ banId, token }) => {
          if (banId === 0 || banId === undefined || !token) {
            return '';
          }
          return {
            url: `/admin/banned-users/${banId}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
      fetchBannedUsers: builder.query<IGetAllBanResponse, IGetAllBanRequest>({
        query: ({ token, page, pageSize, direction }) => {
          if (!token) {
            return '';
          }
          return {
            url: `/admin/banned-users?page=${page}&pageSize=${pageSize}&direction=${direction}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        //@ts-ignore
        providesTags: (result, error, arg) =>
          result
            ? [...result.data.items.map(({ id }) => ({ type: 'BannedUser', id })), { type: 'BannedUser', id: 'LIST' }]
            : [{ type: 'BannedUser', id: 'LIST' }],
      }),

      createBannedUser: builder.mutation<ICreateBannedUserResponse, ICreateBannedUserRequest>({
        query: ({ token, userId, time, adminNotes }) => {
          return {
            url: `/admin/banned-users`,
            method: 'POST',
            body: { token, userId, time, adminNotes },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        invalidatesTags: [{ type: 'BannedUser', id: 'LIST' }],
      }),
      updateBannedUser: builder.mutation<IUpdateBanResponse, IUpdateBanRequest>({
        query: ({ token, banId, adminNotes, time }) => {
          return {
            url: `/admin/banned-users/${banId}`,
            method: 'PATCH',
            body: { adminNotes, time },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        invalidatesTags: (_, error, { banId }) => {
          console.log(error);
          return [
            { type: 'BannedUser', id: banId },
            { type: 'BannedUser', id: 'LIST' },
          ];
        },
      }),
    };
  },
});
export const {
  useDeleteBannedUserMutation,
  useUpdateBannedUserMutation,
  useLazyFetchBannedUserQuery,
  useCreateBannedUserMutation,
  useFetchBannedUsersQuery,
  useLazyFetchBannedUsersQuery,
} = bannedUsersApi;
export { bannedUsersApi };
