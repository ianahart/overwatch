import { http, HttpResponse } from 'msw';
import { IGetTeamMemberTeamResponse } from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { paginate } from '../utils';
import { createTeamMemberTeams } from '../mocks/dbActions';

export const teamMembersHandlers = [
  http.get(`${baseURL}/team-members/:userId/teams`, ({ request }) => {
    const url = new URL(request.url);

    let pg = Number(url.searchParams.get('page')) ?? 1;
    const size = 2;
    const dir = url.searchParams.get('direction') ?? 'next';
    const totalElements = 4;

    const data = createTeamMemberTeams(totalElements);

    const { page, totalPages, pageSize, direction, items } = paginate(pg, size, dir, data);

    return HttpResponse.json<IGetTeamMemberTeamResponse>(
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
        totalTeamMemberTeams: 5,
      },
      { status: 200 }
    );
  }),
];
