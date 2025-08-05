import { http, HttpResponse } from 'msw';

import { ICreateCheckListResponse, IDeleteCheckListResponse } from '../../src/interfaces';
import { baseURL } from '../../src/util';

export const checkListsHandlers = [
  http.post(`${baseURL}/checklists`, () => {
    return HttpResponse.json<ICreateCheckListResponse>(
      {
        message: 'success',
      },
      { status: 201 }
    );
  }),

  http.delete(`${baseURL}/checklists/:id`, () => {
    return HttpResponse.json<IDeleteCheckListResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),
];
