import { http, HttpResponse } from 'msw';
import { baseURL } from '../../src/util';
import { ICreatePaymentRefundResponse } from '../../src/interfaces';

export const paymentRefundsHandlers = [
  http.post(`${baseURL}/payment-refunds`, () => {
    return HttpResponse.json<ICreatePaymentRefundResponse>(
      {
        message: 'success',
      },
      { status: 201 }
    );
  }),
];
