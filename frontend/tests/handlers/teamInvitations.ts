import { http, HttpResponse } from 'msw';
import {
  ICreateTeamInvitationResponse,
  IDeleteTeamInvitationResponse,
  IGetAllTeamInvitationsResponse,
  IUpdateTeamInvitationRequest,
  IUpdateTeamInvitationResponse,
} from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { createTeamInvitations } from '../mocks/dbActions';
import { paginate } from '../utils';

export const teamInvitationsHandlers = [
  http.get(`${baseURL}/teams/invitations`, ({ request }) => {
    const url = new URL(request.url);

    let pg = Number(url.searchParams.get('page')) ?? 1;
    const size = 2;
    const dir = url.searchParams.get('direction') ?? 'next';
    const totalElements = 4;

    const data = createTeamInvitations(totalElements);

    const { page, totalPages, pageSize, direction, items } = paginate(pg, size, dir, data);

    return HttpResponse.json<IGetAllTeamInvitationsResponse>(
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

  http.patch(`${baseURL}/teams/:teamId/invitations/:teamInvitationId`, async ({ request }) => {
    const body = (await request.json()) as IUpdateTeamInvitationRequest;

    if (!body.userId) {
      return HttpResponse.json(
        {
          message: 'Missing userId',
        },
        { status: 400 }
      );
    }
    return HttpResponse.json<IUpdateTeamInvitationResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),

  http.delete(`${baseURL}/teams/invitations/teamInvitationId`, ({ params }) => {
    const { teamInvitationId } = params;

    if (!teamInvitationId) {
      return HttpResponse.json(
        {
          message: 'Missing teamInvitationId',
        },
        { status: 400 }
      );
    }
    return HttpResponse.json<IDeleteTeamInvitationResponse>({ message: 'success' }, { status: 200 });
  }),

  http.post(`${baseURL}/teams/:teamId/invitations`, async ({ params }) => {
    const { teamId } = params;

    if (!teamId) {
      return HttpResponse.json(
        {
          message: 'Missing team id',
        },
        { status: 400 }
      );
    }

    return HttpResponse.json<ICreateTeamInvitationResponse>(
      {
        message: 'success',
      },
      { status: 201 }
    );
  }),
];
