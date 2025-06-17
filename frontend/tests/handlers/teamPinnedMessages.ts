import { http, HttpResponse } from 'msw';
import { ICreateTeamPinnedMessageResponse, IUpdateTeamPinnedMessageResponse } from '../../src/interfaces';
import { baseURL } from '../../src/util';

export const teamPinnedMessagesHandlers = [
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
