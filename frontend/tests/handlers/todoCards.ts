import { http, HttpResponse } from 'msw';
import { toPlainObject } from 'lodash';

import {
  ICreateTodoCardResponse,
  IDeleteTodoCardResponse,
  IReorderTodoCardResponse,
  ITodoCard,
  IUpdateTodoCardResponse,
  IUploadTodoCardResponse,
} from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { db } from '../mocks/db';

export const todoCardsHandlers = [
  http.delete(`${baseURL}/todo-cards/:todoCardId`, () => {
    return HttpResponse.json<IDeleteTodoCardResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),

  http.patch(`${baseURL}/todo-cards/:todoCardId/upload`, () => {
    const data: ITodoCard = {
      ...toPlainObject(db.todoCard.create()),
      title: 'updated title',
      photo: 'https://upload.com/photo',
    };

    return HttpResponse.json<IUploadTodoCardResponse>(
      {
        message: 'success',
        data,
      },
      { status: 200 }
    );
  }),

  http.patch(`${baseURL}/todo-cards/:todoCardId/move`, () => {
    return HttpResponse.json(
      {
        message: 'success',
        data: {},
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
