import { http, HttpResponse } from 'msw';

import { ICreateTeamCommentResponse, IGetTeamCommentResponse, IUpdateTeamCommentResponse } from '../../src/interfaces';
import { baseURL } from '../../src/util';

export const teamCommentsHandlers = [
  http.get(`${baseURL}/team-posts/:teamPostId/team-comments/:teamCommentId`, () => {
    return HttpResponse.json<IGetTeamCommentResponse>(
      {
        message: 'success',
        data: {
          content: 'original content',
          tag: 'original tag',
        },
      },
      { status: 200 }
    );
  }),

  http.patch(`${baseURL}/team-posts/:teamPostId/team-comments/:teamCommentId`, () => {
    return HttpResponse.json<IUpdateTeamCommentResponse>(
      {
        message: 'success',
        data: {
          content: 'updated content',
          tag: 'updated tag',
        },
      },
      { status: 200 }
    );
  }),

  http.post(`${baseURL}/team-posts/:teamPostId/team-comments`, () => {
    return HttpResponse.json<ICreateTeamCommentResponse>(
      {
        message: 'success',
      },
      { status: 201 }
    );
  }),
];
