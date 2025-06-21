import { http, HttpResponse } from 'msw';
import { toPlainObject } from 'lodash';

import {
  ICreateTeamPinnedMessageResponse,
  IDeleteTeamPinnedMessageResponse,
  IGetAllTeamPinnedMessageResponse,
  IReorderTeamPinnedMessageResponse,
  ITeamPinnedMessage,
  IUpdateTeamPinnedMessageResponse,
  IUser,
} from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { createTeamPinnedMessages } from '../mocks/dbActions';
import { db } from '../mocks/db';

export const teamPinnedMessagesHandlers = [
  http.delete(`${baseURL}/teams/:teamId/team-pinned-messages/:teamPinnedMessageId`, () => {
    return HttpResponse.json<IDeleteTeamPinnedMessageResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),

  http.get(`${baseURL}/teams/:teamId/team-pinned-messages`, () => {
    const user: IUser = toPlainObject(db.user.create());
    const data: ITeamPinnedMessage[] = createTeamPinnedMessages(2, user);
    return HttpResponse.json<IGetAllTeamPinnedMessageResponse>(
      {
        message: 'success',
        data,
      },
      { status: 200 }
    );
  }),

  http.post(`${baseURL}/teams/:teamId/team-pinned-messages/reorder`, () => {
    const user: IUser = toPlainObject(db.user.create());
    const data: ITeamPinnedMessage[] = createTeamPinnedMessages(2, user);

    return HttpResponse.json<IReorderTeamPinnedMessageResponse>(
      {
        message: 'success',
        data,
      },
      { status: 200 }
    );
  }),

  http.post(`${baseURL}/teams/:teamId/team-pinned-messages`, () => {
    return HttpResponse.json<ICreateTeamPinnedMessageResponse>(
      {
        message: 'success',
      },
      { status: 201 }
    );
  }),

  http.patch(`${baseURL}/teams/:teamId/team-pinned-messages/:teamPinnedMessageId`, () => {
    return HttpResponse.json<IUpdateTeamPinnedMessageResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),
];
