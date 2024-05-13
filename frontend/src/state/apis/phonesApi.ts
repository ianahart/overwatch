import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreatePhoneRequest,
  ICreatePhoneResponse,
  IDeletePhoneRequest,
  IDeletePhoneResponse,
  IGetPhoneRequest,
  IGetPhoneResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const phonesApi = createApi({
  reducerPath: 'phones',
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      fetchPhone: builder.query<IGetPhoneResponse, IGetPhoneRequest>({
        query: ({ token, userId }) => {
          if (userId === 0) return '';
          return {
            url: '/phones',
            method: 'GET',
            params: {
              userId,
            },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        //@ts-ignore
        invalidatesTags: ['Phone'],
      }),
      createPhone: builder.mutation<ICreatePhoneResponse, ICreatePhoneRequest>({
        query: ({ userId, phoneNumber, token }) => {
          return {
            url: `/phones`,
            method: 'POST',
            body: {
              userId,
              phoneNumber,
            },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
      deletePhone: builder.mutation<IDeletePhoneResponse, IDeletePhoneRequest>({
        query: ({ phoneId, token }) => {
          return {
            url: `/phones/${phoneId}`,
            method: 'DELETE',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
    };
  },
});

export const { useCreatePhoneMutation, useFetchPhoneQuery, useDeletePhoneMutation } = phonesApi;
export { phonesApi };
