import { http, HttpResponse } from 'msw';
import { baseURL } from '../../src/util';
import { IGetUserStripePaymentIntentsResponse } from '../../src/interfaces';
import { createPaymentIntents } from '../mocks/dbActions';
import { paginate } from '../utils';

export const paymentIntentsHandlers = [
  http.get(`${baseURL}/users/:userId/payment-intents`, ({ request }) => {
    const url = new URL(request.url);

    let pg = Number(url.searchParams.get('page')) ?? 1;
    const size = 2;
    const dir = url.searchParams.get('direction') ?? 'next';
    const totalElements = 15;

    const data = createPaymentIntents(totalElements);

    const { page, totalPages, pageSize, direction, items } = paginate(pg, size, dir, data);

    return HttpResponse.json<IGetUserStripePaymentIntentsResponse>(
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
