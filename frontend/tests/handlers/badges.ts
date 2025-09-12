import { http, HttpResponse } from 'msw';

import {
  ICreateBadgeResponse,
  IGetAllReviewerBadgesResponse,
  IGetBadgeResponse,
  IMinAdminBadge,
  IUpdateBadgeResponse,
} from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { createBadges } from '../mocks/dbActions';
import { paginate } from '../utils';
import { faker } from '@faker-js/faker';

export const badgesHandlers = [
  http.get(`${baseURL}/admin/badges/:badgeId`, () => {
    const data: IMinAdminBadge = {
      title: 'badge title',
      imageUrl: faker.image.avatar(),
      description: 'badge description',
    };
    return HttpResponse.json<IGetBadgeResponse>(
      {
        message: 'success',
        data,
      },
      { status: 200 }
    );
  }),

  http.post(`${baseURL}/admin/badges`, () => {
    return HttpResponse.json<ICreateBadgeResponse>(
      {
        message: 'success',
      },
      { status: 201 }
    );
  }),

  http.patch(`${baseURL}/admin/badges/:badgeId`, () => {
    return HttpResponse.json<IUpdateBadgeResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),

  http.get(`${baseURL}/reviewer-badges`, async ({ request }) => {
    const url = new URL(request.url);

    let pg = Number(url.searchParams.get('page')) ?? 1;
    const size = 2;
    const dir = url.searchParams.get('direction') ?? 'next';
    const totalElements = 6;

    const data = createBadges(totalElements);

    const { page, totalPages, pageSize, direction, items } = paginate(pg, size, dir, data);

    return HttpResponse.json<IGetAllReviewerBadgesResponse>(
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
