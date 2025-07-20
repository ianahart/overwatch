import { http, HttpResponse } from 'msw';

import { IGetAllReviewerBadgesResponse } from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { createBadges } from '../mocks/dbActions';
import { paginate } from '../utils';

export const badgesHandlers = [
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
