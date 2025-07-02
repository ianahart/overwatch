import { http, HttpResponse } from 'msw';
import { IFetchPinnedConnectionsResponse } from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { createPinnedConnections } from '../mocks/dbActions';

export const connectionPinsHandlers = [
  http.get(`${baseURL}/connection-pins`, () => {
    const data = createPinnedConnections(2);

    return HttpResponse.json<IFetchPinnedConnectionsResponse>(
      {
        message: 'success',
        data,
      },
      { status: 200 }
    );
  }),
];
