import { http, HttpResponse } from 'msw';
import { toPlainObject } from 'lodash';
import { baseURL } from '../../src/util';
import { db } from '../mocks/db';
import { IFetchSettingsResponse, IUnsubscribeSettingsResponse } from '../../src/interfaces';

export const settingsHandlers = [
  http.get(`${baseURL}/settings/unsubscribe`, () => {
    return HttpResponse.json<IUnsubscribeSettingsResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),

  http.get(`${baseURL}/settings/:settingId`, async () => {
    const settings = toPlainObject(db.setting.create());

    return HttpResponse.json<IFetchSettingsResponse>(
      {
        message: 'success',
        data: settings,
      },
      { status: 200 }
    );
  }),
];
