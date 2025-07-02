import { http, HttpResponse } from 'msw';
import { baseURL } from '../../src/util';
import { ICreatePaymentMethodResponse, ITransferCustomerMoneyToReviewerResponse } from '../../src/interfaces';

export const paymentMethodsHandlers = [
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
