import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateReviewFeedbackRequest,
  ICreateReviewFeedbackResponse,
  IGetSingleReviewFeedbackRequest,
  IGetSingleReviewFeedbackResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const reviewFeedbacksApi = createApi({
  reducerPath: 'reviewFeedbacks',
  tagTypes: ['ReviewFeedback'],
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      getSingleReviewFeedback: builder.query<IGetSingleReviewFeedbackResponse, IGetSingleReviewFeedbackRequest>({
        query: ({ token, reviewerId, ownerId, repositoryId }) => {
          if (token === '') {
            return '';
          }
          return {
            url: `/review-feedbacks/single?reviewerId=${reviewerId}&ownerId=${ownerId}&repositoryId=${repositoryId}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
      createReviewFeedback: builder.mutation<ICreateReviewFeedbackResponse, ICreateReviewFeedbackRequest>({
        query: ({ token, clarity, reviewerId, ownerId, thoroughness, responseTime, helpfulness, repositoryId }) => {
          return {
            url: `/review-feedbacks`,
            method: 'POST',
            headers: {
              Authorization: `Bearer ${token}`,
            },
            body: {
              clarity,
              reviewerId,
              ownerId,
              thoroughness,
              responseTime,
              helpfulness,
              repositoryId,
            },
          };
        },
      }),
    };
  },
});

export const { useLazyGetSingleReviewFeedbackQuery, useCreateReviewFeedbackMutation } = reviewFeedbacksApi;
export { reviewFeedbacksApi };
