import { createApi } from '@reduxjs/toolkit/query/react';
import { ISyncUserResponse, IUpdateUserPasswordResponse, IUpdateUserPasswordRequest } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const usersApi = createApi({
  reducerPath: 'users',
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      updateUserPassword: builder.mutation<IUpdateUserPasswordResponse, IUpdateUserPasswordRequest>({
        query: (payload) => {
          return {
            url: `/users/${payload.userId}/password`,
            method: 'PATCH',
            headers: {
              Authorization: `Bearer ${payload.token}`,
            },
            body: {
              currentPassword: payload.form.curPassword.value,
              newPassword: payload.form.password.value,
            },
          };
        },
      }),
      syncUser: builder.query<ISyncUserResponse, string>({
        query: (token) => {
          return {
            url: '/users/sync',
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
    };
  },
});

export const { useSyncUserQuery, useUpdateUserPasswordMutation } = usersApi;
export { usersApi };
