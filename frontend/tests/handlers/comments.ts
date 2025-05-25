import { http, HttpResponse } from 'msw';
import { toPlainObject } from 'lodash';

import {
  ICreateCommentRequest,
  ICreateCommentResponse,
  IDeleteCommentResponse,
  IGetCommentResponse,
  IGetCommentsResponse,
  IMinComment,
  IUpdateCommentRequest,
  IUpdateCommentResponse,
} from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { createComments, createMinComment } from '../mocks/dbActions';
import { paginate } from '../utils';

export const commentHandlers = [
  http.get(`${baseURL}/topics/:topicId/comments`, async ({ request }) => {
    const url = new URL(request.url);

    let pg = Number(url.searchParams.get('page')) ?? 1;
    const size = 2;
    const dir = url.searchParams.get('direction') ?? 'next';
    const totalElements = 2;

    const data = createComments(totalElements);

    const { page, totalPages, pageSize, direction, items } = paginate(pg, size, dir, data);

    return HttpResponse.json<IGetCommentsResponse>(
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

  http.patch(`${baseURL}/comments/:commentId`, async ({ request }) => {
    const body = (await request.json()) as IUpdateCommentRequest;

    if (!body.userId || !body.content) {
      return HttpResponse.json(
        {
          message: 'Missing userId or commentId',
        },
        { status: 400 }
      );
    }

    return HttpResponse.json<IUpdateCommentResponse>({
      message: 'success',
    });
  }),

  http.get(`${baseURL}/comments/:commentId`, ({ params }) => {
    if (!params.commentId) {
      return HttpResponse.json(
        {
          message: 'Missing commentId',
        },
        { status: 400 }
      );
    }

    const data: IMinComment = toPlainObject(createMinComment());

    return HttpResponse.json<IGetCommentResponse>(
      {
        message: 'success',
        data,
      },
      { status: 200 }
    );
  }),

  http.delete(`${baseURL}/comments/:commentId`, ({ params }) => {
    if (!params.commentId) {
      return HttpResponse.json(
        {
          message: 'Missing commentId',
        },
        { status: 400 }
      );
    }

    return HttpResponse.json<IDeleteCommentResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),

  http.post(`${baseURL}/comments`, async ({ request }) => {
    const body = (await request.json()) as ICreateCommentRequest;

    if (!body.topicId || !body.userId) {
      return HttpResponse.json(
        {
          message: 'Missing topicId or userId',
        },
        { status: 400 }
      );
    }
    return HttpResponse.json<ICreateCommentResponse>(
      {
        message: 'success',
      },
      { status: 201 }
    );
  }),
];
