import { http, HttpResponse } from 'msw';
import {
  IDeleteReplyCommentRequest,
  IDeleteReplyCommentResponse,
  IUpdateReplyCommentRequest,
  IUpdateReplyCommentResponse,
} from '../../src/interfaces';
import { baseURL } from '../../src/util';

export const replyCommentHandlers = [
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

  http.delete(`/${baseURL}/comments/:commentId/reply/:replyCommentId`, async ({ request }) => {
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
