import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { baseURL } from '../../util';
import {
  ISignOut,
  ISignInForm,
  ISignInResponse,
  ISignOutResponse,
  ISignUpForm,
  ISignUpResponse,
  IForgotPasswordForm,
  IForgotPasswordResponse,
  IResetPasswordResponse,
  IResetPasswordBody,
  IVerifyOTPRequest,
  IVerifyOTPResponse,
} from '../../interfaces';

const authsApi = createApi({
  reducerPath: 'auths',
  baseQuery: fetchBaseQuery({
    baseUrl: baseURL,
  }),
  endpoints(builder) {
    return {
      verifyOTP: builder.mutation<IVerifyOTPResponse, IVerifyOTPRequest>({
        query: ({ userId, otpCode }) => {
          return {
            url: '/auth/verify-otp',
            body: { userId, otpCode },
            method: 'POST',
          };
        },
      }),
      fetchOTP: builder.query({
        query: (userId) => {
          if (!userId) return '';
          return {
            url: '/auth/generate-otp',
            params: {
              userId,
            },
            method: 'GET',
          };
        },
      }),
      resetPassword: builder.mutation<IResetPasswordResponse, IResetPasswordBody>({
        query: (form) => {
          return {
            url: '/auth/reset-password',
            body: {
              token: form.token,
              passCode: form.passCode,
              password: form.password,
              confirmPassword: form.confirmPassword,
            },
            method: 'POST',
          };
        },
      }),
      forgotPassword: builder.mutation<IForgotPasswordResponse, IForgotPasswordForm>({
        query: (form) => {
          return {
            url: '/auth/forgot-password',
            body: {
              email: form.email.value,
            },
            method: 'POST',
          };
        },
      }),
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

export const {
  useSignUpMutation,
  useSignInMutation,
  useSignOutMutation,
  useForgotPasswordMutation,
  useResetPasswordMutation,
  useFetchOTPQuery,
  useVerifyOTPMutation,
} = authsApi;
export { authsApi };
