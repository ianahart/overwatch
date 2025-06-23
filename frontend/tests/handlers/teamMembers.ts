import { http, HttpResponse } from 'msw';
import { toPlainObject } from 'lodash';

import {
  IDeleteTeamMemberResponse,
  IGetAllTeamMembersResponse,
  IGetTeamMemberTeamResponse,
  ISearchTeamMembersResponse,
  ITeamMember,
} from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { paginate } from '../utils';
import { createTeamMember, createTeamMemberTeams } from '../mocks/dbActions';
import { db } from '../mocks/db';

export const teamMembersHandlers = [
  http.delete(`${baseURL}/team-members/:teamMemberId`, () => {
    return HttpResponse.json<IDeleteTeamMemberResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),

  http.get(`${baseURL}/teams/:teamId/team-members`, ({ request }) => {
    const url = new URL(request.url);

    let pg = Number(url.searchParams.get('page')) ?? 1;
    const size = 2;
    const dir = url.searchParams.get('direction') ?? 'next';
    const totalElements = 4;

    const data: ITeamMember[] = createTeamMember(4);
    const admin: ITeamMember = toPlainObject(db.teamMember.create());

    const { page, totalPages, pageSize, direction, items } = paginate(pg, size, dir, data);

    return HttpResponse.json<IGetAllTeamMembersResponse>(
      {
        message: 'success',
        admin,
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

  http.get(`${baseURL}/teams/:teamId/team-members/search`, ({ request }) => {
    const url = new URL(request.url);

    let pg = Number(url.searchParams.get('page')) ?? 1;
    const size = 2;
    const dir = url.searchParams.get('direction') ?? 'next';
    const totalElements = 2;

    const data: ITeamMember = toPlainObject(db.teamMember.create({ fullName: 'John Doe' }));

    const { page, totalPages, pageSize, direction, items } = paginate(pg, size, dir, [data]);

    return HttpResponse.json<ISearchTeamMembersResponse>(
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
        totalTeamMemberTeams: 4,
      },
      { status: 200 }
    );
  }),
];
