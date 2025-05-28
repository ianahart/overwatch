import { http, HttpResponse } from 'msw';

import {
  ICreateReviewRequest,
  ICreateReviewResponse,
  IEditReviewRequest,
  IEditReviewResponse,
  IFetchReviewResponse,
} from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { db } from '../mocks/db';

export const reviewsHandlers = [
  http.patch(`${baseURL}/reviews/:reviewId`, async ({ request }) => {
    const body = (await request.json()) as IEditReviewRequest;
    if (!body.rating || !body.reviewerId) {
      return HttpResponse.json(
        {
          message: 'error response',
        },
        { status: 400 }
      );
    }
    return HttpResponse.json<IEditReviewResponse>({
      message: 'success',
    });
  }),

  http.get(`${baseURL}/reviews/:reviewId`, () => {
    const review = db.review.create();

    return HttpResponse.json<IFetchReviewResponse>(
      {
        message: 'success',
        data: {
          id: review.id,
          rating: review.rating,
          review: review.review,
        },
      },
      { status: 200 }
    );
  }),

  http.post(`${baseURL}/reviews`, async ({ request }) => {
    const body = (await request.json()) as ICreateReviewRequest;

    if (!body.reviewerId || !body.authorId || !body.rating) {
      return HttpResponse.json(
        {
          message: 'reviewerId, authorId, rating',
        },
        { status: 400 }
      );
    }
    return HttpResponse.json<ICreateReviewResponse>(
      {
        message: 'success',
      },
      { status: 201 }
    );
  }),
];
