import { http, HttpResponse } from 'msw';

import {
  ICreateWorkSpaceResponse,
  IFetchLatestWorkSpaceResponse,
  IFetchWorkSpacesResponse,
  IUpdateWorkSpaceResponse,
} from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { createWorkSpaces } from '../mocks/dbActions';
import { paginate } from '../utils';

export const workSpacesHandlers = [
  http.patch(`${baseURL}/workspaces/:id`, () => {
    let [data] = createWorkSpaces(1);
    data = { ...data, title: 'updated title' };
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
  http.post(`${baseURL}/workspaces`, () => {
    return HttpResponse.json<ICreateWorkSpaceResponse>(
      {
        message: 'success',
        data: {
          title: 'workspace test',
          backgroundColor: '#123456',
        },
      },
      { status: 201 }
    );
  }),
  http.delete(`${baseURL}/workspaces/:id`, () => {
    return HttpResponse.json(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),

  http.get(`${baseURL}/workspaces/latest`, () => {
    let [workSpace] = createWorkSpaces(1);

    workSpace = { ...workSpace, title: 'test' };

    return HttpResponse.json<IFetchLatestWorkSpaceResponse>(
      {
        message: 'success',
        data: workSpace,
      },
      { status: 200 }
    );
  }),
];
