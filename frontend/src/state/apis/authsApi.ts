import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { baseURL } from '../../util';
import { ISignInForm, ISignInResponse, ISignUpForm, ISignUpResponse } from '../../interfaces';

const authsApi = createApi({
  reducerPath: 'auths',
  baseQuery: fetchBaseQuery({
    baseUrl: baseURL,
  }),
  endpoints(builder) {
    return {
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

export const { useSignUpMutation, useSignInMutation } = authsApi;
export { authsApi };
