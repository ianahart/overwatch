import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateBadgeRequest,
  ICreateBadgeResponse,
  IDeleteBadgeRequest,
  IDeleteBadgeResponse,
  IGetAllBadgesRequest,
  IGetAllBadgesResponse,
  IGetBadgeRequest,
  IGetBadgeResponse,
  IUpdateBadgeRequest,
  IUpdateBadgeResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const badgesApi = createApi({
  reducerPath: 'badges',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['Badge'],
  endpoints(builder) {
    return {
      fetchBadge: builder.query<IGetBadgeResponse, IGetBadgeRequest>({
        query: ({ badgeId, token }) => {
          if (!token || !badgeId) {
            return '';
          }
          return {
            url: `/admin/badges/${badgeId}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
      fetchBadges: builder.query<IGetAllBadgesResponse, IGetAllBadgesRequest>({
        query: ({ token, page, pageSize, direction }) => {
          if (!token) {
            return '';
          }
          return {
            url: `/admin/badges?page=${page}&pageSize=${pageSize}&direction=${direction}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        //@ts-ignore
        providesTags: (result, error, arg) =>
          result
            ? [...result.data.items.map(({ id }) => ({ type: 'Badge', id })), { type: 'Badge', id: 'LIST' }]
            : [{ type: 'Badge', id: 'LIST' }],
      }),
      createBadge: builder.mutation<ICreateBadgeResponse, ICreateBadgeRequest>({
        query: ({ token, body }) => {
          return {
            url: `/admin/badges`,
            method: 'POST',
            body,
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        invalidatesTags: [{ type: 'Badge', id: 'LIST' }],
      }),
      updateBadge: builder.mutation<IUpdateBadgeResponse, IUpdateBadgeRequest>({
        query: ({ token, body, badgeId }) => {
          return {
            url: `/admin/badges/${badgeId}`,
            method: 'PATCH',
            body,
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        invalidatesTags: (_, error, { badgeId }) => {
          console.log(error);
          return [
            { type: 'Badge', id: badgeId },
            { type: 'Badge', id: 'LIST' },
          ];
        },
      }),
      deleteBadge: builder.mutation<IDeleteBadgeResponse, IDeleteBadgeRequest>({
        query: ({ badgeId, token }) => ({
          url: `admin/badges/${badgeId}`,
          method: 'DELETE',
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }),
        //@ts-ignore
        invalidatesTags: (_, error, { badgeId }) => [
          { type: 'Badge', id: badgeId },
          { type: 'Badge', id: 'LIST' },
        ],
      }),
    };
  },
});
export const {
  useDeleteBadgeMutation,
  useCreateBadgeMutation,
  useUpdateBadgeMutation,
  useFetchBadgesQuery,
  useLazyFetchBadgesQuery,
  useLazyFetchBadgeQuery,
} = badgesApi;
export { badgesApi };
