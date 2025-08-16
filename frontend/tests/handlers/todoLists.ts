import { http, HttpResponse } from 'msw';
import { toPlainObject } from 'lodash';

import {
  ICreateTodoListResponse,
  IDeleteTodoListResponse,
  IReorderTodoListResponse,
  ITodoList,
} from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { db } from '../mocks/db';
import { createTodoLists } from '../mocks/dbActions';

export const todoListsHandlers = [
  http.delete(`${baseURL}/todo-lists/:id`, () => {
    return HttpResponse.json<IDeleteTodoListResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),

  http.post(`${baseURL}/workspaces/:workSpaceId/todo-lists/reorder`, () => {
    const todoLists: ITodoList[] = createTodoLists(2);

    return HttpResponse.json<IReorderTodoListResponse>(
      {
        message: 'success',
        data: todoLists,
      },
      { status: 200 }
    );
  }),

  http.post(`${baseURL}/workspaces/:workSpaceId/todo-lists`, () => {
    const todoListEntity = db.todoList.create();

    const data: ITodoList = { ...toPlainObject(todoListEntity), userId: 1, workSpaceId: 1 };

    return HttpResponse.json<ICreateTodoListResponse>(
      {
        message: 'success',
        data,
      },
      { status: 201 }
    );
  }),
];
