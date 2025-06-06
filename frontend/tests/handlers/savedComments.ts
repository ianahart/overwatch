import { http, HttpResponse } from 'msw';
import { baseURL } from '../../src/util';
import { paginate } from '../utils';
import { createSaveComments } from '../mocks/dbActions';
import { db } from '../mocks/db';
import { ICreateSaveCommentRequest, ICreateSaveCommentResponse } from '../../src/interfaces';

export const savedCommentHandlers = [
  http.post(`${baseURL}/save-comments`, async ({ request }) => {
    const body = (await request.json()) as ICreateSaveCommentRequest;

    if (!body.commentId || !body.userId) {
      return HttpResponse.json(
        {
          message: 'Missing commentId or userId',
        },
        { status: 400 }
      );
    }
    return HttpResponse.json<ICreateSaveCommentResponse>(
      {
        message: 'success',
      },
      { status: 201 }
    );
  }),

  http.get(`${baseURL}/save-comments`, async ({ request }) => {
    const url = new URL(request.url);

    let pg = Number(url.searchParams.get('page')) ?? 1;
    const size = Number(url.searchParams.get('pageSize')) ?? 20;
    const dir = url.searchParams.get('direction') ?? 'next';
    const totalElements = 20;

    createSaveComments(totalElements);

    const data = db.saveComment.findMany({});

    const { page, totalPages, pageSize, direction, items } = paginate(pg, size, dir, data);

    return HttpResponse.json(
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
  http.delete(`${baseURL}/save-comments/:commentId`, async () => {
    return HttpResponse.json({
      message: 'success',
    });
  }),
];
