import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { baseURL } from '../../util';
import {
  ISignOut,
  ISignInForm,
  ISignInResponse,
  ISignOutResponse,
  ISignUpForm,
  ISignUpResponse,
} from '../../interfaces';

const authsApi = createApi({
  reducerPath: 'auths',
  baseQuery: fetchBaseQuery({
    baseUrl: baseURL,
  }),
  endpoints(builder) {
    return {
      signOut: builder.mutation<ISignOutResponse, ISignOut>({
        query: (tokens) => {
          return {
            url: '/auth/logout',
            body: {
              refreshToken: tokens.refreshToken,
            },
            headers: {
              Authorization: `Bearer ${tokens.token}`,
            },
            method: 'POST',
          };
        },
      }),
      signIn: builder.mutation<ISignInResponse, ISignInForm>({
        query: (form) => {
          return {
            url: '/auth/login',
            body: {
              email: form.email.value,
              password: form.password.value,
            },
            method: 'POST',
          };
        },
      }),
      signUp: builder.mutation<ISignUpResponse, ISignUpForm>({
        query: (form) => {
          return {
            url: '/auth/register',
            body: {
              firstName: form.firstName.value,
              lastName: form.lastName.value,
              email: form.email.value,
              password: form.password.value,
              confirmPassword: form.confirmPassword.value,
              role: form.role.value,
            },
            method: 'POST',
          };
        },
      }),
    };
  },
});

export const { useSignUpMutation, useSignInMutation, useSignOutMutation } = authsApi;
export { authsApi };
