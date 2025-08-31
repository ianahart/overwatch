import { http, HttpResponse } from 'msw';

import {
  ICreateActiveLabelResponse,
  IDeleteActiveLabelResponse,
  IFetchActiveLabelsResponse,
} from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { createActiveLabels } from '../mocks/dbActions';

export const activeLabelsHandlers = [
  http.delete(`${baseURL}/active-labels/:id`, () => {
    return HttpResponse.json<IDeleteActiveLabelResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),

  http.post(`${baseURL}/active-labels`, () => {
    return HttpResponse.json<ICreateActiveLabelResponse>(
      {
        message: 'success',
      },
      { status: 201 }
    );
  }),

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
