import { http, HttpResponse } from 'msw';

import { IFetchActivityResponse } from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { paginate } from '../utils';
import { createActivities } from '../mocks/dbActions';
import { db } from '../mocks/db';

let data = [
  {
    id: 123,
    text: 'Unique hardcoded activity title',
    createdAt: new Date().toISOString(),
    avatarUrl: 'https://example.com/avatar1.png',
    userId: 1,
    todoCardId: 1,
  },
  ...createActivities(3),
];

export const activitiesHandlers = [
  http.delete(`${baseURL}/activities/:activityId`, ({ params }) => {
    const activityId = Number(params.activityId);

    data = data.filter((activity) => activity.id !== activityId);

    db.activity.delete({
      where: {
        id: { equals: 123 },
      },
    });

    return HttpResponse.json(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),

  http.get(`${baseURL}/todo-cards/:todoCardId/activities`, async ({ request }) => {
    const url = new URL(request.url);
    console.log('GET activities data:', data);

    let pg = Number(url.searchParams.get('page')) ?? 1;
    const size = 2;
    const dir = url.searchParams.get('direction') ?? 'next';
    const totalElements = 4;

    const { page, totalPages, pageSize, direction, items } = paginate(pg, size, dir, data);

    return HttpResponse.json<IFetchActivityResponse>(
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
