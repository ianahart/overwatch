import { http, HttpResponse } from 'msw';
import { IVerifyConnectionResponse } from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { RequestStatus } from '../../src/enums';

export const connectionsHandlers = [
  http.get(`${baseURL}/connections/verify`, async () => {
    return HttpResponse.json<IVerifyConnectionResponse>(
      { message: 'success', data: { id: 2, status: RequestStatus.ACCEPTED } },
      { status: 200 }
    );
  }),
];
