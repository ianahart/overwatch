import { http, HttpResponse } from 'msw';
import { baseURL } from '../../src/util';

export const favoriteHandlers = [
  http.post(`${baseURL}/favorites`, async () => {
    return HttpResponse.json(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),
];
