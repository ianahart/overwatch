import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateActivityRequest,
  ICreateActivityResponse,
  IDeleteActivityRequest,
  IDeleteActivityResponse,
  IFetchActivityRequest,
  IFetchActivityResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const activitiesApi = createApi({
  reducerPath: 'activities',
  tagTypes: ['Activity'],
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      fetchActivities: builder.query<IFetchActivityResponse, IFetchActivityRequest>({
        query: ({ token, todoCardId, page, pageSize, direction }) => {
          if (todoCardId === 0 || todoCardId === null) {
            return '';
          }
          return {
            url: `/todo-cards/${todoCardId}/activities?page=${page}&pageSize=${pageSize}&direction=${direction}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        //@ts-ignore
        providesTags: (result, error, arg) =>
          result
            ? [...result.data.items.map(({ id }) => ({ type: 'Activity', id })), { type: 'Activity', id: 'LIST' }]
            : [{ type: 'Activity', id: 'LIST' }],
      }),

      createActivity: builder.mutation<ICreateActivityResponse, ICreateActivityRequest>({
        query: ({ userId, token, text, todoCardId }) => {
          return {
            url: `/activities`,
            method: 'POST',
            headers: {
              Authorization: `Bearer ${token}`,
            },
            body: {
              userId,
              text,
              todoCardId,
            },
          };
        },
        invalidatesTags: (result, error, {}) => {
          console.log(result, error);
          return [{ type: 'Activity', id: 'LIST' }];
        },
      }),
      deleteActivity: builder.mutation<IDeleteActivityResponse, IDeleteActivityRequest>({
        query: ({ activityId, token }) => ({
          url: `activities/${activityId}`,
          method: 'DELETE',
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }),
        //@ts-ignore
        invalidatesTags: (_, error, { activityId }) => [
          { type: 'Activity', id: activityId },
          { type: 'Activity', id: 'LIST' },
        ],
      }),
    };
  },
});

export const {
  useDeleteActivityMutation,
  useLazyFetchActivitiesQuery,
  useCreateActivityMutation,
  useFetchActivitiesQuery,
} = activitiesApi;
export { activitiesApi };
