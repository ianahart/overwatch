import { http, HttpResponse } from 'msw';
import { IGetAllTeamMessagesResponse } from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { createTeamMessages } from '../mocks/dbActions';

export const teamMessagesHandlers = [
  http.get(`${baseURL}/teams/:currentTeam/team-messages`, () => {
    const totalElements = 10;
    const data = createTeamMessages(totalElements);

    return HttpResponse.json<IGetAllTeamMessagesResponse>(
      {
        message: 'success',
        data,
      },
      { status: 200 }
    );
  }),
];
