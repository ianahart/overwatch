import { http, HttpResponse } from 'msw';
import { toPlainObject } from 'lodash';

import { ICreateTodoListResponse, ITodoList } from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { db } from '../mocks/db';

export const todoListsHandlers = [
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
