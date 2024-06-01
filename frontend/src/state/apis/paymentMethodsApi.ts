import { createApi } from '@reduxjs/toolkit/query/react';
import { ICreatePaymentMethodRequest, ICreatePaymentMethodResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const paymentMethodsApi = createApi({
  reducerPath: 'paymentMethods',
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
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
      }),
    };
  },
});

export const { useCreatePaymentMethodMutation } = paymentMethodsApi;
export { paymentMethodsApi };
