import { http, HttpResponse } from 'msw';
import {
  ICreateTeamInvitationResponse,
  IDeleteTeamInvitationResponse,
  IUpdateTeamInvitationRequest,
  IUpdateTeamInvitationResponse,
} from '../../src/interfaces';
import { baseURL } from '../../src/util';

export const teamInvitationsHandlers = [
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
