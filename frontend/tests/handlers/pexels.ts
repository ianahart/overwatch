import { http, HttpResponse } from 'msw';

import { baseURL } from '../../src/util';
import { IFetchPexelPhotosResponse } from '../../src/interfaces';

export const pexelsHandlers = [
  http.get(`${baseURL}/pexels`, () => {
    const data: string[] = ['https://pexels.com/photo-1', 'https://pexels.com/photo-2', 'https://pexels.com/photo-3'];
    return HttpResponse.json<IFetchPexelPhotosResponse>(
      {
        message: 'success',
        data,
      },
      { status: 200 }
    );
  }),
];
