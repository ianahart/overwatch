import { http, HttpResponse } from 'msw';
import { toPlainObject } from 'lodash';

import { ICreateReviewFeedbackResponse, IGetSingleReviewFeedbackResponse, IReviewFeedback } from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { db } from '../mocks/db';

export const reviewFeedbacksHandlers = [
  http.post(`${baseURL}/review-feedbacks`, () => {
    return HttpResponse.json<ICreateReviewFeedbackResponse>(
      {
        message: 'success',
      },
      { status: 201 }
    );
  }),

  http.get(`${baseURL}/review-feedbacks/single`, () => {
    const data: IReviewFeedback = {
      ...toPlainObject(db.reviewFeedback.create()),
      reviewerId: 1,
      repositoryId: 1,
    };

    return HttpResponse.json<IGetSingleReviewFeedbackResponse>(
      {
        message: 'success',
        data,
      },
      { status: 200 }
    );
  }),
];
