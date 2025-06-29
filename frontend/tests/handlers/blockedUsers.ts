import { http, HttpResponse } from 'msw';
import { baseURL } from '../../src/util';
import { IDeleteBlockedUserResponse, IFetchBlockedUsersResponse } from '../../src/interfaces';
import { paginate } from '../utils';
import { createBlockedUsers } from '../mocks/dbActions';

export const blockedUsersHandlers = [
  http.get(`${baseURL}/block-users`, ({ request }) => {
    const url = new URL(request.url);

    let pg = Number(url.searchParams.get('page')) ?? 1;
    const size = 2;
    const dir = url.searchParams.get('direction') ?? 'next';
    const totalElements = 4;

    const data = createBlockedUsers(totalElements);

    const { page, totalPages, pageSize, direction, items } = paginate(pg, size, dir, data);

    return HttpResponse.json<IFetchBlockedUsersResponse>(
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

  http.delete(`${baseURL}/block-users/:blockUserId`, () => {
    return HttpResponse.json<IDeleteBlockedUserResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),
];
