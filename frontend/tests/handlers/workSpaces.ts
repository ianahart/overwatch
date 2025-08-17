import { http, HttpResponse } from 'msw';

import { IFetchWorkSpacesResponse, IUpdateWorkSpaceResponse } from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { createWorkSpaces } from '../mocks/dbActions';
import { paginate } from '../utils';

export const workSpacesHandlers = [
  http.patch(`${baseURL}/workspaces/:id`, () => {
    const [data] = createWorkSpaces(1);
    return HttpResponse.json<IUpdateWorkSpaceResponse>(
      {
        message: 'success',
        data,
      },
      { status: 200 }
    );
  }),

  http.get(`${baseURL}/workspaces`, ({ request }) => {
    const url = new URL(request.url);

    let pg = Number(url.searchParams.get('page')) ?? 1;
    const size = 2;
    const dir = url.searchParams.get('direction') ?? 'next';
    const totalElements = 4;

    const data = createWorkSpaces(totalElements);

    const { page, totalPages, pageSize, direction, items } = paginate(pg, size, dir, data);

    return HttpResponse.json<IFetchWorkSpacesResponse>(
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
