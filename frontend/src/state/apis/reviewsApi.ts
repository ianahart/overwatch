import { createApi } from '@reduxjs/toolkit/query/react';
import { ICreateReviewRequest, ICreateReviewResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const reviewsApi = createApi({
  reducerPath: 'reviews',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['Review'],
  endpoints(builder) {
    return {
      createReview: builder.mutation<ICreateReviewResponse, ICreateReviewRequest>({
        query: ({ authorId, reviewerId, token, rating, review }) => {
          return {
            url: `/reviews`,
            method: 'POST',
            body: { authorId, reviewerId, rating, review },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        invalidatesTags: [{ type: 'Review', id: 'LIST' }],
      }),
    };
  },
});
export const { useCreateReviewMutation } = reviewsApi;
export { reviewsApi };
