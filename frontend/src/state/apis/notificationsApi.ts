import { createApi } from '@reduxjs/toolkit/query/react';
import {
  IDeleteNotificationRequest,
  IDeleteNotificationResponse,
  IFetchNotificationsRequest,
  IFetchNotificationsResponse,
} from '../../interfaces';
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
      deleteNotification: builder.mutation<IDeleteNotificationResponse, IDeleteNotificationRequest>({
        query: ({ notificationId, token, notificationRole, senderId, receiverId }) => ({
          url: `notifications/${notificationId}?notificationRole=${notificationRole}&senderId=${senderId}&receiverId=${receiverId}`,
          method: 'DELETE',
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }),
        //@ts-ignore
        invalidatesTags: (_, error, { notificationId }) => [
          { type: 'Notification', id: notificationId },
          { type: 'Notification', id: 'LIST' },
        ],
      }),
    };
  },
});
export const { useDeleteNotificationMutation, useFetchNotificationsQuery, useLazyFetchNotificationsQuery } =
  notificationsApi;
export { notificationsApi };
