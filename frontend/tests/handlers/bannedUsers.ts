import { http, HttpResponse } from 'msw';

import {
  ICreateBannedUserResponse,
  IDeleteBanResponse,
  IGetAllBanResponse,
  IGetBanResponse,
  IUpdateBanResponse,
} from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { createBannedUsers } from '../mocks/dbActions';
import { paginate } from '../utils';

export const bannedUsersHandlers = [
  http.get(`${baseURL}/admin/banned-users`, async ({ request }) => {
    const url = new URL(request.url);

    let pg = Number(url.searchParams.get('page')) ?? 1;
    const size = 2;
    const dir = url.searchParams.get('direction') ?? 'next';
    const totalElements = 6;

    const data = createBannedUsers(size);

    const { page, totalPages, pageSize, direction, items } = paginate(pg, size, dir, data);

    return HttpResponse.json<IGetAllBanResponse>(
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

  http.patch(`${baseURL}/admin/banned-users/:banId`, () => {
    let [data] = createBannedUsers(1);
    data = { ...data, adminNotes: 'updated notes' };

    return HttpResponse.json<IUpdateBanResponse>(
      {
        message: 'success',
        data,
      },
      { status: 200 }
    );
  }),

  http.get(`${baseURL}/admin/banned-users/:banId`, () => {
    let [data] = createBannedUsers(1);
    data = { ...data, fullName: 'john doe', adminNotes: 'existing notes' };

    return HttpResponse.json<IGetBanResponse>(
      {
        message: 'success',
        data,
      },
      { status: 200 }
    );
  }),

  http.delete(`${baseURL}/admin/banned-users/:banId`, () => {
    return HttpResponse.json<IDeleteBanResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),

  http.post(`${baseURL}/admin/banned-users`, () => {
    return HttpResponse.json<ICreateBannedUserResponse>(
      {
        message: 'success',
      },
      { status: 201 }
    );
  }),
];
