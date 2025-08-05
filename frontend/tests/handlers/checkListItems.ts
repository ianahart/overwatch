import { http, HttpResponse } from 'msw';

import { ICreateCheckListItemResponse } from '../../src/interfaces';
import { baseURL } from '../../src/util';

export const checkListsItemsHandlers = [
  http.post(`${baseURL}/checklist-items`, () => {
    return HttpResponse.json<ICreateCheckListItemResponse>(
      {
        message: 'success',
        data: { id: 1, userId: 1, checkListId: 1, title: 'new title', isCompleted: false },
      },
      { status: 201 }
    );
  }),
];
