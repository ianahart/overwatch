import { http, HttpResponse } from 'msw';
import { baseURL } from '../../src/util';
import { ICreateTeamPostResponse, IDeleteTeamPostResponse } from '../../src/interfaces';

export const teamPostsHandlers = [
  http.delete(`${baseURL}/team-posts/:teamPostId`, () => {
    return HttpResponse.json<IDeleteTeamPostResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),

  http.post(`${baseURL}/teams/:teamId/team-posts`, () => {
    return HttpResponse.json<ICreateTeamPostResponse>(
      {
        message: 'success',
      },
      { status: 201 }
    );
  }),
];
