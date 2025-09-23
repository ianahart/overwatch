import { http, HttpResponse } from 'msw';

import {
  ICreateReportCommentRequest,
  ICreateReportCommentResponse,
  IDeleteReportCommentResponse,
  IGetAllReportCommentResponse,
} from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { createReportComments } from '../mocks/dbActions';
import { paginate } from '../utils';

export const reportCommentHandlers = [
  http.get(`${baseURL}/admin/report-comments`, async ({ request }) => {
    const url = new URL(request.url);

    let pg = Number(url.searchParams.get('page')) ?? 1;
    const size = 2;
    const dir = url.searchParams.get('direction') ?? 'next';
    const totalElements = 6;

    const data = createReportComments(totalElements);

    const { page, totalPages, pageSize, direction, items } = paginate(pg, size, dir, data);

    return HttpResponse.json<IGetAllReportCommentResponse>(
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
