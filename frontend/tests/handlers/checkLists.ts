import { http, HttpResponse } from 'msw';

import { ICreateCheckListResponse, IDeleteCheckListResponse, IFetchCheckListsResponse } from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { mockCheckLists } from '../mocks/data';

export const checkListsHandlers = [
  http.get(`${baseURL}/todo-cards/:todoCardId/checklists`, () => {
    return HttpResponse.json<IFetchCheckListsResponse>(
      {
        message: 'success',
        data: mockCheckLists,
      },
      { status: 200 }
    );
  }),

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
