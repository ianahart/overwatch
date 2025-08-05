import { http, HttpResponse } from 'msw';

import { IDeleteCheckListResponse } from '../../src/interfaces';
import { baseURL } from '../../src/util';

export const checkListsHandlers = [
  http.delete(`${baseURL}/checklists/:id`, () => {
    return HttpResponse.json<IDeleteCheckListResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),
];
