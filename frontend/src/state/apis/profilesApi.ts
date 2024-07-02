import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateAvatarRequest,
  ICreateAvatarResponse,
  IFetchAllProfileRequest,
  IFetchAllProfileResponse,
  IFetchFullProfileRequest,
  IFetchFullProfileResponse,
  IFetchProfileRequest,
  IFetchProfileResponse,
  IRemoveAvatarRequest,
  IRemoveAvatarResponse,
  IUpdateProfileRequest,
  IUpdateProfileResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const profilesApi = createApi({
  reducerPath: 'profiles',
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      fetchAllProfile: builder.query<IFetchAllProfileResponse, IFetchAllProfileRequest>({
        query: ({ token, page, pageSize, direction, filter }) => {
          if (!token) {
            return '';
          }
          return {
            url: `/profiles/all/${filter}?page=${page}&pageSize=${pageSize}&direction=${direction}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),

      fetchProfile: builder.query<IFetchFullProfileResponse, IFetchFullProfileRequest>({
        query: ({ token, profileId }) => {
          if (profileId === 0 || profileId === undefined || !token) {
            return '';
          }
          return {
            url: `/profiles/${profileId}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
      fetchPopulateProfile: builder.query<IFetchProfileResponse, IFetchProfileRequest>({
        query: ({ token, profileId }) => {
          if (profileId === 0 || profileId === undefined) {
            return '';
          }
          return {
            url: `/profiles/${profileId}/populate`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        // @ts-ignore
        providesTags: ['Profile'],
      }),
      updateProfile: builder.mutation<IUpdateProfileResponse, IUpdateProfileRequest<object>>({
        query: ({ profileId, formData, token }) => {
          return {
            url: `/profiles/${profileId}`,
            method: 'PATCH',
            body: formData,
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        // @ts-ignore
        invalidatesTags: ['Profile'],
      }),
      removeAvatar: builder.mutation<IRemoveAvatarResponse, IRemoveAvatarRequest>({
        query: ({ profileId, avatarUrl, avatarFilename, token }) => {
          return {
            url: `/profiles/${profileId}/avatar/remove`,
            method: 'PATCH',
            body: { avatarUrl, avatarFilename },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
      createAvatar: builder.mutation<ICreateAvatarResponse, ICreateAvatarRequest>({
        query: ({ profileId, formData, token }) => {
          return {
            url: `/profiles/${profileId}/avatar/update`,
            method: 'PATCH',
            body: formData,
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
  useFetchAllProfileQuery,
  useLazyFetchAllProfileQuery,
  useFetchProfileQuery,
  useFetchPopulateProfileQuery,
  useUpdateProfileMutation,
  useCreateAvatarMutation,
  useRemoveAvatarMutation,
} = profilesApi;
export { profilesApi };
