import { http, HttpResponse } from 'msw';

import { IDeleteLabelResponse, IFetchLabelResponse } from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { createLabels } from '../mocks/dbActions';

export const labelsHandlers = [
  http.delete(`${baseURL}/labels/:id`, () => {
    return HttpResponse.json<IDeleteLabelResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),

  http.get(`${baseURL}/labels`, () => {
    const data = createLabels(3);

    return HttpResponse.json<IFetchLabelResponse>(
      {
        message: 'success',
        data,
      },
      { status: 200 }
    );
  }),
];
