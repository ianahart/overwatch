import { createApi } from '@reduxjs/toolkit/query/react';
import {
  IGetAllStripePaymentIntentsRequest,
  IGetAllStripePaymentIntentsResponse,
  IGetUserStripePaymentIntentsRequest,
  IGetUserStripePaymentIntentsResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const stripePaymentIntentsApi = createApi({
  reducerPath: 'stripePaymentIntent',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['PaymentItent'],
  endpoints(builder) {
    return {
      exportPaymentIntentsToPdf: builder.query<void, IGetAllStripePaymentIntentsRequest>({
        queryFn: async ({ token, page, pageSize, direction, search }, _queryApi, _extraOptions, baseQuery) => {
          if (!token || !search) {
            return { error: { status: 400, data: 'Invalid request parameters' } };
          }

          const result = await baseQuery({
            url: `/admin/payment-intents/export-pdf?&page=${page}&pageSize=${pageSize}&direction=${direction}&search=${search}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
              Accept: 'application/pdf',
            },
            responseHandler: (response) => response.blob(),
          });

          if (result.error) {
            return { error: result.error };
          }

          const blob = result.data as Blob;
          const url = window.URL.createObjectURL(blob);
          const a = document.createElement('a');
          a.href = url;
          a.download = 'transactions.pdf';
          document.body.appendChild(a);
          a.click();
          window.URL.revokeObjectURL(url);
          return { data: undefined };
        },
      }),
      fetchAllPaymentIntents: builder.query<IGetAllStripePaymentIntentsResponse, IGetAllStripePaymentIntentsRequest>({
        query: ({ token, page, pageSize, direction, search }) => {
          if (!token || !search) {
            return '';
          }
          return {
            url: `/admin/payment-intents?&page=${page}&pageSize=${pageSize}&direction=${direction}&search=${search}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        //@ts-ignore
        providesTags: (result, error, arg) =>
          result
            ? [
                ...result.data.result.items.map(({ id }) => ({ type: 'PaymentItent', id })),
                { type: 'PaymentItent', id: 'LIST' },
              ]
            : [{ type: 'PaymentItent', id: 'LIST' }],
      }),

      fetchUserPaymentIntents: builder.query<IGetUserStripePaymentIntentsResponse, IGetUserStripePaymentIntentsRequest>(
        {
          query: ({ token, page, pageSize, direction, userId }) => {
            if (!token || !userId) {
              return '';
            }
            return {
              url: `/users/${userId}/payment-intents?&page=${page}&pageSize=${pageSize}&direction=${direction}`,
              method: 'GET',
              headers: {
                Authorization: `Bearer ${token}`,
              },
            };
          },
          //@ts-ignore
          providesTags: (result, error, arg) =>
            result
              ? [
                  ...result.data.items.map(({ id }) => ({ type: 'PaymentItent', id })),
                  { type: 'PaymentItent', id: 'LIST' },
                ]
              : [{ type: 'PaymentItent', id: 'LIST' }],
        }
      ),
    };
  },
});
export const {
  useLazyFetchAllPaymentIntentsQuery,
  useLazyFetchUserPaymentIntentsQuery,
  useLazyExportPaymentIntentsToPdfQuery,
} = stripePaymentIntentsApi;
export { stripePaymentIntentsApi };
