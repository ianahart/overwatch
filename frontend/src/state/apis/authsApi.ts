import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { baseURL } from '../../util';
import { ISignUpForm, ISignUpResponse } from '../../interfaces';

const authsApi = createApi({
  reducerPath: 'auths',
  baseQuery: fetchBaseQuery({
    baseUrl: baseURL,
  }),
  endpoints(builder) {
    return {
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

export const { useSignUpMutation } = authsApi;
export { authsApi };
