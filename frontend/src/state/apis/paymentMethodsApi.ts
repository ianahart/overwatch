import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreatePaymentMethodRequest,
  ICreatePaymentMethodResponse,
  IGetPaymentMethodRequest,
  IGetPaymentMethodResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const paymentMethodsApi = createApi({
  reducerPath: 'paymentMethods',
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      fetchPaymentMethod: builder.query<IGetPaymentMethodResponse, IGetPaymentMethodRequest>({
        query: ({ token, userId }) => {
          if (userId === null || userId === 0) {
            return '';
          }
          return {
            url: `/users/${userId}/payment-methods`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        // @ts-ignore
        providesTags: ['PaymentMethods'],
      }),
      createPaymentMethod: builder.mutation<ICreatePaymentMethodResponse, ICreatePaymentMethodRequest>({
        query: ({ body, token, userId }) => {
          return {
            url: `/users/${userId}/payment-methods`,
            method: 'POST',
            body,
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        // @ts-ignore
        invalidatesTags: ['PaymentMethods'],
      }),
    };
  },
});

export const { useCreatePaymentMethodMutation, useFetchPaymentMethodQuery } = paymentMethodsApi;
export { paymentMethodsApi };
