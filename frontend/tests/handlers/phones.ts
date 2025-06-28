import { http, HttpResponse } from 'msw';
import { toPlainObject } from 'lodash';
import { baseURL } from '../../src/util';
import { db } from '../mocks/db';
import { IGetPhoneResponse, IPhone } from '../../src/interfaces';

export const phonesHandlers = [
  http.get(`${baseURL}/phones`, async () => {
    const data: IPhone = { ...toPlainObject(db.phone.create()) };

    return HttpResponse.json<IGetPhoneResponse>(
      {
        message: 'success',
        data,
      },
      { status: 200 }
    );
  }),
];
