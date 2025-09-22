import { http, HttpResponse } from 'msw';

import {
  ICreateReportCommentRequest,
  ICreateReportCommentResponse,
  IDeleteReportCommentResponse,
} from '../../src/interfaces';
import { baseURL } from '../../src/util';

export const reportCommentHandlers = [
  http.delete(`${baseURL}/admin/report-comments/:id`, () => {
    return HttpResponse.json<IDeleteReportCommentResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),

  http.post(`${baseURL}/report-comments`, async ({ request }) => {
    const body = (await request.json()) as ICreateReportCommentRequest;

    if (!body.reason || !body.userId || !body.details) {
      return HttpResponse.json(
        {
          message: 'Missing reason, details, or userId',
        },
        { status: 400 }
      );
    }
    return HttpResponse.json<ICreateReportCommentResponse>(
      {
        message: 'success',
      },
      { status: 201 }
    );
  }),
];
