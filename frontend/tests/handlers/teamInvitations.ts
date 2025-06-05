import { http, HttpResponse } from 'msw';
import { ICreateTeamInvitationResponse } from '../../src/interfaces';
import { baseURL } from '../../src/util';

export const teamInvitationsHandlers = [
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
