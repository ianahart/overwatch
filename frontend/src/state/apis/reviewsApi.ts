import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateReviewRequest,
  ICreateReviewResponse,
  IDeleteReviewRequest,
  IDeleteReviewResponse,
  IEditReviewRequest,
  IEditReviewResponse,
  IFetchReviewRequest,
  IFetchReviewResponse,
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
      fetchReview: builder.query<IFetchReviewResponse, IFetchReviewRequest>({
        query: ({ reviewId, token }) => {
          if (reviewId === 0 || reviewId === undefined) {
            return '';
          }
          return {
            url: `/reviews/${reviewId}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),

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
      editReview: builder.mutation<IEditReviewResponse, IEditReviewRequest>({
        query: ({ reviewId, authorId, reviewerId, token, rating, review }) => {
          return {
            url: `/reviews/${reviewId}`,
            method: 'PATCH',
            body: { authorId, reviewerId, rating, review },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        invalidatesTags: (_, error, { reviewId }) => {
          console.log(error);
          return [
            { type: 'Review', id: reviewId },
            { type: 'Review', id: 'LIST' },
          ];
        },
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
      deleteReview: builder.mutation<IDeleteReviewResponse, IDeleteReviewRequest>({
        query: ({ reviewId, token }) => ({
          url: `reviews/${reviewId}`,
          method: 'DELETE',
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }),
        //@ts-ignore
        invalidatesTags: (_, error, { reviewId }) => [
          { type: 'Review', id: reviewId },
          { type: 'Review', id: 'LIST' },
        ],
      }),
    };
  },
});
export const {
  useDeleteReviewMutation,
  useCreateReviewMutation,
  useLazyFetchReviewsQuery,
  useFetchReviewsQuery,
  useFetchReviewQuery,
  useEditReviewMutation,
} = reviewsApi;
export { reviewsApi };
