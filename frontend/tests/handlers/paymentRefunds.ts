import { http, HttpResponse } from 'msw';
import { baseURL } from '../../src/util';
import {
  ICreatePaymentRefundResponse,
  IGetAllStripePaymentRefundResponse,
  IUpdatePaymentRefundResponse,
} from '../../src/interfaces';
import { paginate } from '../utils';
import { createRefunds } from '../mocks/dbActions';

export const paymentRefundsHandlers = [
  http.post(`${baseURL}/payment-refunds`, () => {
    return HttpResponse.json<ICreatePaymentRefundResponse>(
      {
        message: 'success',
      },
      { status: 201 }
    );
  }),

  http.patch(`${baseURL}/admin/:userId/payment-refunds/:id`, () => {
    return HttpResponse.json<IUpdatePaymentRefundResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),

  http.get(`${baseURL}/admin/:userId/payment-refunds`, ({ request }) => {
    const url = new URL(request.url);

    let pg = Number(url.searchParams.get('page')) ?? 1;
    const size = 2;
    const dir = url.searchParams.get('direction') ?? 'next';
    const totalElements = 2;

    const data = createRefunds(totalElements);

    const { page, totalPages, pageSize, direction, items } = paginate(pg, size, dir, data);

    return HttpResponse.json<IGetAllStripePaymentRefundResponse>(
      {
        message: 'success',
        data: {
          items,
          page,
          pageSize,
          totalPages,
          direction,
          totalElements,
        },
      },
      { status: 200 }
    );
  }),
];
