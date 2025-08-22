import { http, HttpResponse } from 'msw';
import { toPlainObject } from 'lodash';
import {
  ICreateCustomFieldResponse,
  ICustomField,
  IDeleteCustomFieldResponse,
  IFetchCustomFieldsResponse,
  IUpdateCustomFieldResponse,
} from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { db } from '../mocks/db';

export const customFieldsHandlers = [
  http.post(`${baseURL}/custom-fields`, () => {
    return HttpResponse.json<ICreateCustomFieldResponse>(
      {
        message: 'success',
      },
      { status: 201 }
    );
  }),

  http.get(`${baseURL}/todo-cards/:todoCardId/custom-fields`, () => {
    const data: ICustomField[] = Array.from({ length: 3 }).map(() => {
      const customField: ICustomField = {
        ...toPlainObject(db.customField.create()),
        userId: 1,
        todoCardId: 1,
        dropDownOptions: [],
      };
      return customField;
    });
    return HttpResponse.json<IFetchCustomFieldsResponse>(
      {
        message: 'success',
        data,
      },
      { status: 200 }
    );
  }),

  http.patch(`${baseURL}/custom-fields/:id`, () => {
    return HttpResponse.json<IUpdateCustomFieldResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),
  http.delete(`${baseURL}/custom-fields/:id`, () => {
    return HttpResponse.json<IDeleteCustomFieldResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),
];
