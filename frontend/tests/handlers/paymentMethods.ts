import { http, HttpResponse } from 'msw';
import { baseURL } from '../../src/util';
import { ITransferCustomerMoneyToReviewerResponse } from '../../src/interfaces';

export const paymentMethodsHandlers = [
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
