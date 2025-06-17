import { http, HttpResponse } from 'msw';
import { toPlainObject } from 'lodash';

import { ICreateTeamRequest, ICreateTeamResponse, IGetTeamResponse, ITeam } from '../../src/interfaces';
import { db } from '../mocks/db';
import { baseURL } from '../../src/util';

export const teamsHandlers = [
  http.get(`${baseURL}/teams/:teamId`, () => {
    const team: ITeam = toPlainObject(db.team.create());

    return HttpResponse.json<IGetTeamResponse>(
      {
        message: 'success',
        data: team,
      },
      { status: 200 }
    );
  }),

  http.post(`${baseURL}/teams`, async ({ request }) => {
    const body = (await request.json()) as ICreateTeamRequest;

    if (!body.teamName) {
      return HttpResponse.json(
        {
          message: 'Missing team name',
        },
        { status: 400 }
      );
    }

    return HttpResponse.json<ICreateTeamResponse>(
      {
        message: 'success',
      },
      { status: 201 }
    );
  }),
];
