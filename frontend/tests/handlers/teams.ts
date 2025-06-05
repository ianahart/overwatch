import { http, HttpResponse } from 'msw';
import { ICreateTeamRequest, ICreateTeamResponse } from '../../src/interfaces';
import { baseURL } from '../../src/util';

export const teamsHandlers = [
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
