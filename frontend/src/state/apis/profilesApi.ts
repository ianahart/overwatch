import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateAvatarRequest,
  ICreateAvatarResponse,
  IFetchAllProfileRequest,
  IFetchAllProfileResponse,
  IFetchFullProfileRequest,
  IFetchFullProfileResponse,
  IFetchProfilePackageRequest,
  IFetchProfilePackageResponse,
  IFetchProfileRequest,
  IFetchProfileResponse,
  IFetchProfileVisibilityRequest,
  IFetchProfileVisibilityResponse,
  IRemoveAvatarRequest,
  IRemoveAvatarResponse,
  IUpdateProfileRequest,
  IUpdateProfileResponse,
  IUpdateProfileVisibilityRequest,
  IUpdateProfileVisibilityResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const profilesApi = createApi({
  reducerPath: 'profiles',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['Profile'],
  endpoints(builder) {
    return {
      fetchProfilePackages: builder.query<IFetchProfilePackageResponse, IFetchProfilePackageRequest>({
        query: ({ token, userId }) => {
          if (userId === 0 || userId === undefined || token === '' || !token) {
            return '';
          }
          return {
            url: `/profiles/packages?userId=${userId}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        // @ts-ignore
        providesTags: ['Profile'],
      }),
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
            console.log('RUN!!!!!!');
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
        providesTags: ['Profile', 'ProfileVisibility'],
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
      updateProfileVisibility: builder.mutation<IUpdateProfileVisibilityResponse, IUpdateProfileVisibilityRequest>({
        query: ({ profileId, isVisible, token }) => {
          return {
            url: `/profiles/${profileId}/visibility`,
            method: 'PATCH',
            body: { isVisible },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        //@ts-ignore
        invalidatesTags: (result, error, { profileId }) => [{ type: 'ProfileVisibility', id: profileId }],
      }),
      fetchProfileVisibility: builder.query<IFetchProfileVisibilityResponse, IFetchProfileVisibilityRequest>({
        query: ({ token, profileId }) => {
          if (profileId === 0 || profileId === undefined || !token) {
            return '';
          }
          return {
            url: `/profiles/${profileId}/visibility`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        // @ts-ignore
        providesTags: (result, error, { profileId }) => [{ type: 'ProfileVisibility', id: profileId }],
      }),
    };
  },
});

export const {
  useLazyFetchProfilePackagesQuery,
  useFetchProfileVisibilityQuery,
  useUpdateProfileVisibilityMutation,
  useFetchAllProfileQuery,
  useLazyFetchAllProfileQuery,
  useFetchProfileQuery,
  useFetchPopulateProfileQuery,
  useUpdateProfileMutation,
  useCreateAvatarMutation,
  useRemoveAvatarMutation,
} = profilesApi;
export { profilesApi };
