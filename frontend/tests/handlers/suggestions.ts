import { http, HttpResponse } from 'msw';

import { ICreateSuggestionResponse } from '../../src/interfaces';
import { baseURL } from '../../src/util';

export const suggestionsHandlers = [
  http.post(`${baseURL}/suggestions`, () => {
    return HttpResponse.json<ICreateSuggestionResponse>(
      {
        message: 'success',
      },
      { status: 201 }
    );
  }),
];
