import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateReviewRequest,
  ICreateReviewResponse,
  IFetchReviewsRequest,
  IFetchReviewsResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const reviewsApi = createApi({
  reducerPath: 'reviews',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['Review'],
  endpoints(builder) {
    return {
      fetchReviews: builder.query<IFetchReviewsResponse, IFetchReviewsRequest>({
        query: ({ userId, token, page, pageSize, direction }) => {
          if (userId === 0 || userId === null) {
            return '';
          }
          return {
            url: `/reviews?userId=${userId}&page=${page}&pageSize=${pageSize}&direction=${direction}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        //@ts-ignore
        providesTags: (result, error, arg) =>
          result
            ? [...result.data.items.map(({ id }) => ({ type: 'Review', id })), { type: 'Review', id: 'LIST' }]
            : [{ type: 'Review', id: 'LIST' }],
      }),

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
export const { useCreateReviewMutation, useLazyFetchReviewsQuery, useFetchReviewsQuery } = reviewsApi;
export { reviewsApi };
