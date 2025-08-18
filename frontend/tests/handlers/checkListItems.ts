import { http, HttpResponse } from 'msw';

import {
  ICreateCheckListItemResponse,
  IDeleteCheckListItemResponse,
  IUpdateCheckListItemResponse,
} from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { mockCheckLists } from '../mocks/data';

export const checkListsItemsHandlers = [
  http.delete(`${baseURL}/checklist-items/:id`, ({ params }) => {
    const id = Number(params.id);

    for (const checkList of mockCheckLists) {
      checkList.checkListItems = checkList.checkListItems.filter((item) => item.id !== id);
    }

    return HttpResponse.json<IDeleteCheckListItemResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),

  http.put(`${baseURL}/checklist-items/:id`, () => {
    return HttpResponse.json<IUpdateCheckListItemResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),

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
