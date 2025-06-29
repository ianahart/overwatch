import { http, HttpResponse } from 'msw';
import { baseURL } from '../../src/util';
import { IDeleteBlockedUserResponse } from '../../src/interfaces';

export const phonesHandlers = [
  http.delete(`${baseURL}/block-users/:blockUserId`, () => {
    return HttpResponse.json<IDeleteBlockedUserResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),
];
