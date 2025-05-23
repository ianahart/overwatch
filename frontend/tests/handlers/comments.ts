import { http, HttpResponse } from 'msw';
import { toPlainObject } from 'lodash';

import { IDeleteCommentResponse, IGetCommentResponse, IMinComment } from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { createMinComment } from '../mocks/dbActions';

export const commentHandlers = [
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
];
