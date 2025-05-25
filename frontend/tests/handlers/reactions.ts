import { http, HttpResponse } from 'msw';

import { baseURL } from '../../src/util';
import { ICreateReactionResponse, IDeleteReactionResponse, IGetReactionResponse } from '../../src/interfaces';
//import { db } from '../mocks/db';

export const reactionHandlers = [
  http.delete(`${baseURL}/comments/:commentId/reactions/:userId`, () => {
    console.log('Delete handler intercepted:');
    return HttpResponse.json<IDeleteReactionResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),

  http.post(`${baseURL}/comments/:commentId/reactions`, () => {
    return HttpResponse.json<ICreateReactionResponse>(
      {
        message: 'success',
      },
      { status: 201 }
    );
  }),

  http.get(`${baseURL}/comments/:commentId/reactions`, ({ params }) => {
    if (!params.commentId) {
      return HttpResponse.json({
        message: 'Missing commentId for reactions',
      });
    }

    //const reaction = db.reaction.create();

    return HttpResponse.json<IGetReactionResponse>(
      {
        message: 'success',
        data: '👍',
      },
      { status: 200 }
    );
  }),
];
