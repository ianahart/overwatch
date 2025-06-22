import { http, HttpResponse } from 'msw';
import { toPlainObject } from 'lodash';

import {
  ICreateTeamRequest,
  ICreateTeamResponse,
  IGetAllTeamsResponse,
  IGetTeamResponse,
  ITeam,
} from '../../src/interfaces';
import { db } from '../mocks/db';
import { baseURL } from '../../src/util';
import { createTeams } from '../mocks/dbActions';
import { paginate } from '../utils';

export const teamsHandlers = [
  http.get(`${baseURL}/teams`, ({ request }) => {
    const url = new URL(request.url);

    let pg = Number(url.searchParams.get('page')) ?? 1;
    const size = 2;
    const dir = url.searchParams.get('direction') ?? 'next';
    const totalElements = 2;

    const data = createTeams(totalElements);

    data[0].totalTeams = 4;

    const { page, totalPages, pageSize, direction, items } = paginate(pg, size, dir, data);

    return HttpResponse.json<IGetAllTeamsResponse>(
      {
        message: 'success',
        data: {
          items,
          page,
          pageSize,
          totalPages,
          direction,
          totalElements,
        },
      },
      { status: 200 }
    );
  }),

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
