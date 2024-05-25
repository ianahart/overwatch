import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateAvatarRequest,
  ICreateAvatarResponse,
  IRemoveAvatarRequest,
  IRemoveAvatarResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const profilesApi = createApi({
  reducerPath: 'profiles',
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
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

export const { useCreateAvatarMutation, useRemoveAvatarMutation } = profilesApi;
export { profilesApi };
