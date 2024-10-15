import { createApi } from '@reduxjs/toolkit/query/react';
import {
  IFetchSettingsRequest,
  IFetchSettingsResponse,
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
      fetchSettings: builder.query<IFetchSettingsResponse | undefined, IFetchSettingsRequest>({
        query: ({ settingId, token }) => {
          if (settingId === 0) return '';
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

export const { useUpdateSettingsMutation, useUpdateSettingsMFAMutation, useFetchSettingsQuery } = settingsApi;
export { settingsApi };
