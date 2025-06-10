import { http, HttpResponse } from 'msw';
import { baseURL } from '../../src/util';
import { ICreateTeamPostResponse } from '../../src/interfaces';

export const teamPostsHandlers = [
  http.post(`${baseURL}/teams/:teamId/team-posts`, () => {
    return HttpResponse.json<ICreateTeamPostResponse>(
      {
        message: 'success',
      },
      { status: 201 }
    );
  }),
];
