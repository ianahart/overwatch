import { http, HttpResponse } from 'msw';
import {
  ICreateConnectionRequest,
  ICreateConnectionResponse,
  IDeleteConnectionResponse,
  IVerifyConnectionResponse,
} from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { RequestStatus } from '../../src/enums';

export const connectionsHandlers = [
  http.delete(`${baseURL}/connections/:connectionId`, ({ params }) => {
    if (!params.connectionId) {
      return HttpResponse.json(
        {
          message: 'Missing param of connectionId',
        },
        { status: 400 }
      );
    }

    return HttpResponse.json<IDeleteConnectionResponse>({
      message: 'success',
    });
  }),

  http.post(`${baseURL}/connections`, async ({ request }) => {
    const body = (await request.json()) as ICreateConnectionRequest;

    if (!body.receiverId || !body.senderId) {
      return HttpResponse.json(
        {
          message: 'Missing either receiverId or senderId',
        },
        { status: 400 }
      );
    }

    return HttpResponse.json<ICreateConnectionResponse>(
      {
        message: 'success',
      },
      { status: 201 }
    );
  }),

  http.get(`${baseURL}/connections/verify`, async () => {
    return HttpResponse.json<IVerifyConnectionResponse>(
      { message: 'success', data: { id: 2, status: RequestStatus.ACCEPTED } },
      { status: 200 }
    );
  }),
];
