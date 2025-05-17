import { http, HttpResponse } from 'msw';
import { baseURL } from '../../src/util';
import { paginate } from '../utils';
import { createMinProfiles } from '../mocks/dbActions';

export const profileHandlers = [
  http.get(`${baseURL}/profiles/all/:filter`, async ({ request }) => {
    const url = new URL(request.url);

    let pg = Number(url.searchParams.get('page')) ?? 1;
    const size = Number(url.searchParams.get('pageSize')) ?? 10;
    const dir = url.searchParams.get('direction') ?? 'next';
    const totalElements = 20;

    const data = createMinProfiles(10);

    const { page, totalPages, pageSize, direction, items } = paginate(pg, size, dir, data);

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
