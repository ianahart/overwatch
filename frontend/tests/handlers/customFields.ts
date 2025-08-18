import { http, HttpResponse } from 'msw';
import { toPlainObject } from 'lodash';
import { ICustomField, IFetchCustomFieldsResponse } from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { db } from '../mocks/db';

export const customFieldsHandlers = [
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
];
