import { http, HttpResponse } from 'msw';

import {
  ICreateReviewRequest,
  ICreateReviewResponse,
  IDeleteReviewRequest,
  IDeleteReviewResponse,
  IEditReviewRequest,
  IEditReviewResponse,
  IFetchReviewResponse,
  IFetchReviewsResponse,
} from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { db } from '../mocks/db';
import { paginate } from '../utils';
import { createReviews } from '../mocks/dbActions';

export const reviewsHandlers = [
  http.get(`${baseURL}/reviews`, async ({ request }) => {
    const url = new URL(request.url);

    let pg = Number(url.searchParams.get('page')) ?? 1;
    const size = 2;
    const dir = url.searchParams.get('direction') ?? 'next';
    const totalElements = 10;

    const data = createReviews(totalElements);

    const { page, totalPages, pageSize, direction, items } = paginate(pg, size, dir, data);

    return HttpResponse.json<IFetchReviewsResponse>(
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

  http.delete(`${baseURL}/reviews/:reviewId`, async ({ request }) => {
    const body = (await request.json()) as IDeleteReviewRequest;
    if (!body.reviewId) {
      return HttpResponse.json(
        {
          message: 'error response',
        },
        { status: 400 }
      );
    }
    return HttpResponse.json<IDeleteReviewResponse>({
      message: 'success',
    });
  }),

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
