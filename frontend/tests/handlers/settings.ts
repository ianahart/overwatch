import { http, HttpResponse } from 'msw';
import { toPlainObject } from 'lodash';
import { baseURL } from '../../src/util';
import { db } from '../mocks/db';
import {
  IFetchSettingsResponse,
  IUnsubscribeSettingsResponse,
  IUpdateSettingResponse,
  IUpdateSettingsMFAResponse,
} from '../../src/interfaces';

export const settingsHandlers = [
  http.put(`${baseURL}/settings/:settingId`, () => {
    const setting = toPlainObject(db.setting.create());

    return HttpResponse.json<IUpdateSettingResponse>(
      {
        message: 'success',
        data: setting,
      },
      { status: 200 }
    );
  }),

  http.patch(`${baseURL}/settings/:settingId/mfa-enabled`, () => {
    return HttpResponse.json<IUpdateSettingsMFAResponse>(
      {
        message: 'success',
        mfaEnabled: true,
      },
      { status: 200 }
    );
  }),

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
