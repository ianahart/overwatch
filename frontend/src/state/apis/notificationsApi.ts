import { createApi } from '@reduxjs/toolkit/query/react';
import { IFetchNotificationsRequest, IFetchNotificationsResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const notificationsApi = createApi({
  reducerPath: 'notifications',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['Notification'],
  endpoints(builder) {
    return {
      fetchNotifications: builder.query<IFetchNotificationsResponse, IFetchNotificationsRequest>({
        query: ({ userId, token, page, pageSize, direction }) => {
          if (userId === 0 || userId === null) {
            return '';
          }
          return {
            url: `/notifications?userId=${userId}&page=${page}&pageSize=${pageSize}&direction=${direction}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        //@ts-ignore
        providesTags: (result, error, arg) =>
          result
            ? [
                ...result.data.items.map(({ id }) => ({ type: 'Notification', id })),
                { type: 'Notification', id: 'LIST' },
              ]
            : [{ type: 'Notification', id: 'LIST' }],
      }),
      //  deleteReview: builder.mutation<IDeleteReviewResponse, IDeleteReviewRequest>({
      //    query: ({ reviewId, token }) => ({
      //      url: `reviews/${reviewId}`,
      //      method: 'DELETE',
      //      headers: {
      //        Authorization: `Bearer ${token}`,
      //      },
      //    }),
      //    //@ts-ignore
      //    invalidatesTags: (_, error, { reviewId }) => [
      //      { type: 'Review', id: reviewId },
      //      { type: 'Review', id: 'LIST' },
      //    ],
      //  }),
    };
  },
});
export const { useFetchNotificationsQuery, useLazyFetchNotificationsQuery } = notificationsApi;
export { notificationsApi };
