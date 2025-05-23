import { http, HttpResponse } from 'msw';

import { baseURL } from '../../src/util';
import { IGetReactionResponse } from '../../src/interfaces';
import { db } from '../mocks/db';

export const reactionHandlers = [
  http.get(`${baseURL}/comments/:commentId/reactions`, ({ params }) => {
    if (!params.commentId) {
      return HttpResponse.json({
        message: 'Missing commentId for reactions',
      });
    }

    const reaction = db.reaction.create();

    return HttpResponse.json<IGetReactionResponse>(
      {
        message: 'success',
        data: reaction.emoji,
      },
      { status: 200 }
    );
  }),
];
