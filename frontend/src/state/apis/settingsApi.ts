import { createApi } from '@reduxjs/toolkit/query/react';
import {
  IFetchSettingsRequest,
  IFetchSettingsResponse,
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
    };
  },
});

export const { useUpdateSettingsMFAMutation, useFetchSettingsQuery } = settingsApi;
export { settingsApi };
