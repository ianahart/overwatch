import { http, HttpResponse } from 'msw';
import { baseURL } from '../../src/util';
import { IFetchRepositoriesResponse } from '../../src/interfaces';
import { paginate } from '../utils';
import { createRepositories } from '../mocks/dbActions';

export const repositoriesHandlers = [
  http.get(`${baseURL}/repositories`, async ({ request }) => {
    const url = new URL(request.url);

    let pg = Number(url.searchParams.get('page')) ?? 1;
    const size = 2;
    const dir = url.searchParams.get('direction') ?? 'next';
    const totalElements = 4;

    const data = createRepositories(totalElements, { status: 'COMPLETED' });

    const { page, totalPages, pageSize, direction, items } = paginate(pg, size, dir, data);

    return HttpResponse.json<IFetchRepositoriesResponse>(
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
