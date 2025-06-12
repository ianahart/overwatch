import { http, HttpResponse } from 'msw';
import { baseURL } from '../../src/util';
import { ICreateTeamPostResponse, IDeleteTeamPostResponse, IGetAllTeamPostsResponse } from '../../src/interfaces';
import { createTeamPosts } from '../mocks/dbActions';
import { paginate } from '../utils';

export const teamPostsHandlers = [
  http.get(`${baseURL}/teams/:teamId/team-posts`, ({ request }) => {
    const url = new URL(request.url);

    let pg = Number(url.searchParams.get('page')) ?? 1;
    const size = 2;
    const dir = url.searchParams.get('direction') ?? 'next';
    const totalElements = 4;

    const data = createTeamPosts(totalElements);

    const { page, totalPages, pageSize, direction, items } = paginate(pg, size, dir, data);

    return HttpResponse.json<IGetAllTeamPostsResponse>(
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
        totalTeamMemberTeams: 0,
      },
      { status: 200 }
    );
  }),

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
