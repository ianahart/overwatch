import { createApi } from '@reduxjs/toolkit/query/react';
import {
  IFetchSettingsRequest,
  IFetchSettingsResponse,
  IUnsubscribeSettingsRequest,
  IUnsubscribeSettingsResponse,
  IUpdateSettingRequest,
  IUpdateSettingResponse,
  IUpdateSettingsMFARequest,
  IUpdateSettingsMFAResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const settingsApi = createApi({
  reducerPath: 'settings',
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      unsubscribeEmailSettings: builder.query<IUnsubscribeSettingsResponse, IUnsubscribeSettingsRequest>({
        query: ({ email }) => {
          return {
            url: `/settings/unsubscribe?email=${email}`,
            method: 'GET',
          };
        },
        // @ts-ignoree
        invalidatesTags: ['Setting'],
      }),

      fetchSettings: builder.query<IFetchSettingsResponse | undefined, IFetchSettingsRequest>({
        query: ({ settingId, token }) => {
          if (settingId === 0 || !token) return '';
          return {
            url: `/settings/${settingId}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        // @ts-ignoree
        invalidatesTags: ['Setting'],
      }),
      updateSettingsMFA: builder.mutation<IUpdateSettingsMFAResponse, IUpdateSettingsMFARequest>({
        query: ({ mfaEnabled, settingId, token }) => {
          return {
            url: `/settings/${settingId}/mfa-enabled`,
            method: 'PATCH',
            body: {
              mfaEnabled,
            },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
      updateSettings: builder.mutation<IUpdateSettingResponse, IUpdateSettingRequest>({
        query: ({ setting, token }) => {
          return {
            url: `/settings/${setting.id}`,
            method: 'PUT',
            body: { setting },
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
  useUnsubscribeEmailSettingsQuery,
  useUpdateSettingsMutation,
  useUpdateSettingsMFAMutation,
  useFetchSettingsQuery,
} = settingsApi;
export { settingsApi };
