import { http, HttpResponse } from 'msw';
import { toPlainObject } from 'lodash';
import {
  IDeleteUserResponse,
  IGetAllAdminUsersResponse,
  IGetAllReviewersResponse,
  IReviewer,
  ISyncUserResponse,
  IUpdateUserPasswordResponse,
  IUpdateUserResponse,
} from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { getLoggedInUser, paginate } from '../utils';
import { createReviewers, createViewUsers } from '../mocks/dbActions';
import { db } from '../mocks/db';

export const usersHandlers = [
  http.get(`${baseURL}/users/sync`, () => {
    const { curUser } = toPlainObject(getLoggedInUser());

    return HttpResponse.json<ISyncUserResponse>(...curUser, { status: 200 });
  }),

  http.post(`${baseURL}/users/:userId/delete`, () => {
    return HttpResponse.json<IDeleteUserResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),

  http.patch(`${baseURL}/users/:userId/password`, () => {
    return HttpResponse.json<IUpdateUserPasswordResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),

  http.patch(`${baseURL}/users/:userId`, () => {
    return HttpResponse.json<IUpdateUserResponse>(
      {
        message: 'success',
        data: {
          firstName: 'John',
          lastName: 'Doe',
          email: 'test@example.com',
          abbreviation: 'J.D',
        },
      },
      { status: 200 }
    );
  }),

  http.get(`${baseURL}/users/search`, ({ request }) => {
    const url = new URL(request.url);

    let pg = Number(url.searchParams.get('page')) ?? 1;
    const size = 2;
    const dir = url.searchParams.get('direction') ?? 'next';
    const totalElements = 6;

    // static reviewer for detailed testing purposes
    const staticReviewer: IReviewer = { ...toPlainObject(db.reviewer.create()), fullName: 'john doe' };
    const data = [staticReviewer, ...createReviewers(totalElements)];

    const { page, totalPages, pageSize, direction, items } = paginate(pg, size, dir, data);

    return HttpResponse.json<IGetAllReviewersResponse>(
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
  http.get(`${baseURL}/admin/users`, ({ request }) => {
    const url = new URL(request.url);

    let pg = Number(url.searchParams.get('page')) ?? 1;
    const size = 2;
    const dir = url.searchParams.get('direction') ?? 'next';
    const totalElements = 6;

    const data = createViewUsers(size);

    const { page, totalPages, pageSize, direction, items } = paginate(pg, size, dir, data);

    return HttpResponse.json<IGetAllAdminUsersResponse>(
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
