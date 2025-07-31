import { http, HttpResponse } from 'msw';

import { IFetchActiveLabelsResponse } from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { createActiveLabels } from '../mocks/dbActions';

export const activeLabelsHandlers = [
  http.get(`${baseURL}/active-labels`, () => {
    const data = createActiveLabels(3);

    return HttpResponse.json<IFetchActiveLabelsResponse>(
      {
        message: 'success',
        data,
      },
      { status: 200 }
    );
  }),
];
