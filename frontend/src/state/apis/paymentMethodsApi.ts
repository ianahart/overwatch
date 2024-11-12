import { createApi } from '@reduxjs/toolkit/query/react';
import {
  IConnectStripeAccountRequest,
  IConnectStripeAccountResponse,
  ICreatePaymentMethodRequest,
  ICreatePaymentMethodResponse,
  IDeletePaymentMethodRequest,
  IDeletePaymentMethodResponse,
  IGetPaymentMethodRequest,
  IGetPaymentMethodResponse,
  ITransferCustomerMoneyToReviewerRequest,
  ITransferCustomerMoneyToReviewerResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const paymentMethodsApi = createApi({
  reducerPath: 'paymentMethods',
  tagTypes: ['PaymentMethods'],
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      deletePaymentMethod: builder.mutation<IDeletePaymentMethodResponse, IDeletePaymentMethodRequest>({
        query: ({ token, id }) => {
          return {
            url: `/payment-methods/${id}`,
            method: 'DELETE',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
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
      transferCustomerMoneyToReviewer: builder.mutation<
        ITransferCustomerMoneyToReviewerResponse,
        ITransferCustomerMoneyToReviewerRequest
      >({
        query: ({ ownerId, token, reviewerId, repositoryId }) => {
          return {
            url: `/payment-methods`,
            method: 'POST',
            body: { reviewerId, ownerId, repositoryId },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        // @ts-ignore
        invalidatesTags: ['PaymentMethods'],
      }),
      connectAccount: builder.mutation<IConnectStripeAccountResponse, IConnectStripeAccountRequest>({
        query: ({ email, token, userId }) => {
          return {
            url: `/users/${userId}/payment-methods/connect`,
            method: 'POST',
            body: { email },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
    };
  },
});

export const {
  useTransferCustomerMoneyToReviewerMutation,
  useConnectAccountMutation,
  useDeletePaymentMethodMutation,
  useCreatePaymentMethodMutation,
  useFetchPaymentMethodQuery,
} = paymentMethodsApi;
export { paymentMethodsApi };
