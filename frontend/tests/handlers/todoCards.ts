import { http, HttpResponse } from 'msw';
import { toPlainObject } from 'lodash';

import { ICreateTodoCardResponse, ITodoCard } from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { db } from '../mocks/db';

export const todoCardsHandlers = [
  http.post(`${baseURL}/todo-lists/:todoListId/todo-cards`, async () => {
    const todoCardEntity = db.todoCard.create();

    const data: ITodoCard = { ...toPlainObject(todoCardEntity), userId: 1, todoListId: 1 };

    return HttpResponse.json<ICreateTodoCardResponse>(
      {
        message: 'success',
        data,
      },
      { status: 201 }
    );
  }),
];
