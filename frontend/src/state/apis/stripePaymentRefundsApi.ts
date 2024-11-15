import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreatePaymentRefundRequest,
  ICreatePaymentRefundResponse,
  IGetAllStripePaymentRefundRequest,
  IGetAllStripePaymentRefundResponse,
  IUpdatePaymentRefundRequest,
  IUpdatePaymentRefundResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const stripePaymentRefundsApi = createApi({
  reducerPath: 'stripePaymentRefunds',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['PaymentRefund', 'PaymentIntent'],
  endpoints(builder) {
    return {
      fetchPaymentRefunds: builder.query<IGetAllStripePaymentRefundResponse, IGetAllStripePaymentRefundRequest>({
        query: ({ userId, token, page, pageSize, direction }) => {
          if (userId === 0 || userId === null || !token) {
            return '';
          }
          return {
            url: `/admin/${userId}/payment-refunds?&page=${page}&pageSize=${pageSize}&direction=${direction}`,
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
                ...result.data.items.map(({ id }) => ({ type: 'PaymentRefund', id })),
                { type: 'PaymentRefund', id: 'LIST' },
              ]
            : [{ type: 'PaymentRefund', id: 'LIST' }],
      }),

      createPaymentRefund: builder.mutation<ICreatePaymentRefundResponse, ICreatePaymentRefundRequest>({
        query: ({ token, userId, stripePaymentIntentId, reason }) => {
          return {
            url: '/payment-refunds',
            method: 'POST',
            body: { userId, stripePaymentIntentId, reason },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        invalidatesTags: [{ type: 'PaymentIntent', id: 'LIST' }],
      }),
      updatePaymentRefund: builder.mutation<IUpdatePaymentRefundResponse, IUpdatePaymentRefundRequest>({
        query: ({ token, userId, stripePaymentIntentId, adminNotes, id, status }) => {
          return {
            url: `/admin/${userId}/payment-refunds/${id}`,
            method: 'PATCH',
            body: { stripePaymentIntentId, adminNotes, status },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        //@ts-ignore
        invalidatesTags: (_, error, { id }) => {
          return [
            { type: 'PaymentRefund', id },
            { type: 'PaymentRefund', id: 'LIST' },
          ];
        },
      }),
    };
  },
});
export const { useUpdatePaymentRefundMutation, useCreatePaymentRefundMutation, useLazyFetchPaymentRefundsQuery } =
  stripePaymentRefundsApi;
export { stripePaymentRefundsApi };
