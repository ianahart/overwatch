import { http, HttpResponse } from 'msw';

import { IDeleteDropDownOptionResponse } from '../../src/interfaces';
import { baseURL } from '../../src/util';

export const dropDownOptionsHandlers = [
  http.delete(`${baseURL}/dropdown-options/:id`, () => {
    return HttpResponse.json<IDeleteDropDownOptionResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),
];
