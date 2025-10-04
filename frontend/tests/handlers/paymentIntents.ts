import { http, HttpResponse } from 'msw';
import { baseURL } from '../../src/util';
import { IGetAllStripePaymentIntentsResponse, IGetUserStripePaymentIntentsResponse } from '../../src/interfaces';
import { createPaymentIntents, createPaymentIntentTransactions } from '../mocks/dbActions';
import { paginate } from '../utils';

export const paymentIntentsHandlers = [
  http.get(`${baseURL}/admin/payment-intents`, async ({ request }) => {
    const url = new URL(request.url);

    let pg = Number(url.searchParams.get('page')) ?? 1;
    const size = 2;
    const dir = url.searchParams.get('direction') ?? 'next';
    const totalElements = 6;

    const data = createPaymentIntentTransactions(totalElements);

    const { page, totalPages, pageSize, direction, items } = paginate(pg, size, dir, data);

    return HttpResponse.json<IGetAllStripePaymentIntentsResponse>(
      {
        message: 'success',
        data: {
          result: {
            items,
            page,
            pageSize,
            totalPages,
            direction,
            totalElements,
          },
          revenue: 10000,
        },
      },
      { status: 200 }
    );
  }),

  http.get(`${baseURL}/admin/payment-intents/export-pdf`, async () => {
    const blob = new Blob(['FAKE_PDF_DATA'], { type: 'application/pdf' });
    return new HttpResponse(blob, {
      status: 200,
      headers: { 'Content-Type': 'application/pdf' },
    });
  }),

  http.get(`${baseURL}/admin/payment-intents/export-csv`, async () => {
    const csvContent = 'id,amount,status\n1,100,paid\n2,200,failed\n';
    const blob = new Blob([csvContent], { type: 'text/csv' });

    return new HttpResponse(blob, {
      status: 200,
      headers: {
        'Content-Type': 'text/csv',
      },
    });
  }),

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
