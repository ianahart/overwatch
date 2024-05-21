import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ISyncUserResponse,
  IUpdateUserPasswordResponse,
  IUpdateUserPasswordRequest,
  IDeleteUserRequest,
  IDeleteUserResponse,
  IUpdateUserResponse,
  IUpdateUserRequest,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const usersApi = createApi({
  reducerPath: 'users',
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      deleteUser: builder.mutation<IDeleteUserResponse, IDeleteUserRequest>({
        query: ({ userId, token, password }) => {
          return {
            url: `/users/${userId}/delete`,
            method: 'POST',
            headers: {
              Authorization: `Bearer ${token}`,
            },
            body: {
              password,
            },
          };
        },
      }),
      updateUser: builder.mutation<IUpdateUserResponse, IUpdateUserRequest>({
        query: ({ form, token, userId }) => {
          return {
            url: `/users/${userId}`,
            method: 'PATCH',
            headers: {
              Authorization: `Bearer ${token}`,
            },
            body: {
              firstName: form.firstName.value,
              lastName: form.lastName.value,
              email: form.email.value,
            },
          };
        },
      }),
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

export const { useSyncUserQuery, useUpdateUserPasswordMutation, useDeleteUserMutation, useUpdateUserMutation } =
  usersApi;
export { usersApi };
