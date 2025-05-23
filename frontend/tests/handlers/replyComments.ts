import { http, HttpResponse } from 'msw';
import {
  IDeleteReplyCommentRequest,
  IDeleteReplyCommentResponse,
  IGetReplyCommentsByUserResponse,
  IUpdateReplyCommentRequest,
  IUpdateReplyCommentResponse,
} from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { createReplyComments } from '../mocks/dbActions';
import { paginate } from '../utils';

export const replyCommentHandlers = [
  http.get(`${baseURL}/comments/:commentId/reply/user/:userId`, ({ request }) => {
    const url = new URL(request.url);

    let pg = Number(url.searchParams.get('page')) ?? 1;
    const size = 2;
    const dir = url.searchParams.get('direction') ?? 'next';
    const totalElements = 10;

    const data = createReplyComments(20);

    const { page, totalPages, pageSize, direction, items } = paginate(pg, size, dir, data);

    return HttpResponse.json<IGetReplyCommentsByUserResponse>(
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

  http.patch(`${baseURL}/comments/:commentId/reply/:replyCommentId`, async ({ request, params }) => {
    const body = (await request.json()) as IUpdateReplyCommentRequest;

    if (!params.commentId || !params.replyCommentId) {
      return HttpResponse.json(
        {
          message: 'Missing commentId or replyCommentId',
        },
        { status: 400 }
      );
    }
    return HttpResponse.json<IUpdateReplyCommentResponse>(
      {
        message: 'success',
        data: body.content,
      },
      { status: 200 }
    );
  }),

  http.delete(`${baseURL}/comments/:commentId/reply/:replyCommentId`, async ({ request }) => {
    const body = (await request.json()) as IDeleteReplyCommentRequest;

    if (!body.commentId || !body.replyCommentId) {
      return HttpResponse.json(
        {
          message: 'Missing commentId or replyCommentId',
        },
        { status: 400 }
      );
    }

    return HttpResponse.json<IDeleteReplyCommentResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),
];
