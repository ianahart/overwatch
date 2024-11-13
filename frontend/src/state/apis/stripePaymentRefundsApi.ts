import { createApi } from '@reduxjs/toolkit/query/react';
import { ICreatePaymentRefundRequest, ICreatePaymentRefundResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const stripePaymentRefundsApi = createApi({
  reducerPath: 'stripePaymentRefunds',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['PaymentRefund', 'PaymentIntent'],
  endpoints(builder) {
    return {
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
    };
  },
});
export const { useCreatePaymentRefundMutation } = stripePaymentRefundsApi;
export { stripePaymentRefundsApi };
