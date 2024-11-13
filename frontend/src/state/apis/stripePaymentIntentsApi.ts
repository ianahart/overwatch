import { createApi } from '@reduxjs/toolkit/query/react';
import { IGetAllStripePaymentIntentsRequest, IGetAllStripePaymentIntentsResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const stripePaymentIntentsApi = createApi({
  reducerPath: 'stripePaymentIntent',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['PaymentItent'],
  endpoints(builder) {
    return {
      fetchPaymentIntents: builder.query<IGetAllStripePaymentIntentsResponse, IGetAllStripePaymentIntentsRequest>({
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
      }),
    };
  },
});
export const { useLazyFetchPaymentIntentsQuery } = stripePaymentIntentsApi;
export { stripePaymentIntentsApi };
