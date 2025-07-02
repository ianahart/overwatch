import { http, HttpResponse } from 'msw';
import { baseURL } from '../../src/util';
import {
  ICreatePaymentMethodResponse,
  IDeletePaymentMethodResponse,
  ITransferCustomerMoneyToReviewerResponse,
} from '../../src/interfaces';

export const paymentMethodsHandlers = [
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
