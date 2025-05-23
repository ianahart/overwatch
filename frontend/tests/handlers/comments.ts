import { http, HttpResponse } from 'msw';
import { toPlainObject } from 'lodash';

import { IGetCommentResponse, IMinComment } from '../../src/interfaces';
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
];
