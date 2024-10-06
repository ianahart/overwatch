import { createApi } from '@reduxjs/toolkit/query/react';
import { ICreateReviewFeedbackRequest, ICreateReviewFeedbackResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const reviewFeedbacksApi = createApi({
  reducerPath: 'reviewFeedbacks',
  tagTypes: ['ReviewFeedback'],
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      createReviewFeedback: builder.mutation<ICreateReviewFeedbackResponse, ICreateReviewFeedbackRequest>({
        query: ({ token, clarity, reviewerId, ownerId, thoroughness, responseTime, helpfulness }) => {
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
            },
          };
        },
      }),
    };
  },
});

export const { useCreateReviewFeedbackMutation } = reviewFeedbacksApi;
export { reviewFeedbacksApi };
