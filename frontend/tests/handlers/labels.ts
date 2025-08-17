import { http, HttpResponse } from 'msw';

import { IFetchLabelResponse } from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { createLabels } from '../mocks/dbActions';

export const labelsHandlers = [
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
