import { http, HttpResponse } from 'msw';
import { toPlainObject } from 'lodash';

import {
  ICreateTodoCardResponse,
  IReorderTodoCardResponse,
  ITodoCard,
  IUpdateTodoCardResponse,
} from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { db } from '../mocks/db';

export const todoCardsHandlers = [
  http.patch(`${baseURL}/todo-cards/:todoCardId/move`, () => {
    return HttpResponse.json<IReorderTodoCardResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),

  http.patch(`${baseURL}/todo-cards/:todoCardId/reorder`, () => {
    return HttpResponse.json<IReorderTodoCardResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),

  http.put(`${baseURL}/todo-cards/:id`, () => {
    const data: ITodoCard = { ...toPlainObject(db.todoCard.create()), title: 'updated title' };

    return HttpResponse.json<IUpdateTodoCardResponse>(
      {
        message: 'success',
        data,
      },
      { status: 200 }
    );
  }),

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
