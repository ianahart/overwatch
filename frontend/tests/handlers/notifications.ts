import { http, HttpResponse } from 'msw';
import { baseURL } from '../../src/util';
import { paginate, toPlainObject } from '../utils';
import { createUser, createNotifications } from '../mocks/dbActions';
import { db } from '../mocks/db';

export const notificationHandlers = [
  http.delete(`${baseURL}/notifications/:notificationId`, async ({ params }) => {
    const { notificationId } = params;
    db.notification.delete({
      where: {
        id: {
          equals: Number(notificationId),
        },
      },
    });

    return HttpResponse.json({
      message: 'success',
    });
  }),

  http.get(`${baseURL}/notifications`, async ({ request }) => {
    const url = new URL(request.url);

    let pg = Number(url.searchParams.get('page')) ?? 1;
    const size = Number(url.searchParams.get('pageSize')) ?? 10;
    const dir = url.searchParams.get('direction') ?? 'next';
    const totalElements = 10;

    const receiver = createUser({ id: 1 });
    const sender = createUser({ id: 2 });

    const data = createNotifications(1, receiver, sender);

    let { page, totalPages, pageSize, direction, items } = paginate(pg, size, dir, data);

    items = items.map((item) => toPlainObject(item));

    return HttpResponse.json(
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
