import { http, HttpResponse } from 'msw';
import { baseURL } from '../../src/util';
import {
  ICreatePaymentMethodResponse,
  IDeletePaymentMethodResponse,
  IGetPaymentMethodResponse,
  ITransferCustomerMoneyToReviewerResponse,
} from '../../src/interfaces';

export const paymentMethodsHandlers = [
  http.get(`${baseURL}/users/:userId/payment-methods`, () => {
    return HttpResponse.json<IGetPaymentMethodResponse>(
      {
        message: 'success',
        data: {
          stripeEnabled: false,
          id: 123,
          last4: '4242',
          displayBrand: 'visa',
          expMonth: 12,
          expYear: 2026,
          name: 'Test User',
        },
      },
      { status: 200 }
    );
  }),

  http.delete(`${baseURL}/payment-methods/:id`, () => {
    return HttpResponse.json<IDeletePaymentMethodResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),

  http.post(`${baseURL}/users/:userId/payment-methods`, () => {
    return HttpResponse.json<ICreatePaymentMethodResponse>(
      {
        message: 'success',
      },
      { status: 201 }
    );
  }),

  http.post(`${baseURL}/payment-methods`, () => {
    return HttpResponse.json<ITransferCustomerMoneyToReviewerResponse>(
      {
        message: 'success',
        data: {},
      },
      { status: 201 }
    );
  }),
];
