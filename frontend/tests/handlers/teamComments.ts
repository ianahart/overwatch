import { http, HttpResponse } from 'msw';

import {
  ICreateTeamCommentResponse,
  IDeleteTeamCommentResponse,
  IGetAllTeamCommentsResponse,
  IGetTeamCommentResponse,
  IUpdateTeamCommentResponse,
} from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { createTeamComments, createTeamMember } from '../mocks/dbActions';
import { paginate } from '../utils';

export const teamCommentsHandlers = [
  http.get(`${baseURL}/team-posts/:teamPostId/team-comments`, ({ request }) => {
    const url = new URL(request.url);

    let pg = Number(url.searchParams.get('page')) ?? 1;
    const size = 2;
    const dir = url.searchParams.get('direction') ?? 'next';
    const totalElements = 10;

    const data = createTeamComments(totalElements);
    const [admin] = createTeamMember(1);

    const { page, totalPages, pageSize, direction, items } = paginate(pg, size, dir, data);

    return HttpResponse.json<IGetAllTeamCommentsResponse>(
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
        admin,
      },
      { status: 200 }
    );
  }),

  http.delete(`${baseURL}/team-posts/:teamPostId/team-comments/:teamCommentId`, () => {
    return HttpResponse.json<IDeleteTeamCommentResponse>(
      {
        message: 'success',
        data: 'content',
      },
      { status: 200 }
    );
  }),

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
