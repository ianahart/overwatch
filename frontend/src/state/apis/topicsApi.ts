import { createApi } from '@reduxjs/toolkit/query/react';
import { ICreateTopicRequest, ICreateTopicResponse, IGetTopicsRequest, IGetTopicsResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const topicsApi = createApi({
  reducerPath: 'topics',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['Topic'],
  endpoints(builder) {
    return {
      fetchTopics: builder.query<IGetTopicsResponse, IGetTopicsRequest>({
        query: ({ query }) => {
          return {
            url: `/topics/search?query=${query}`,
            method: 'GET',
            headers: {},
          };
        },
        //@ts-ignore
        providesTags: (result, error, arg) =>
          result
            ? [...result.data.map(({ id }) => ({ type: 'Topic', id })), { type: 'Topic', id: 'LIST' }]
            : [{ type: 'Topic', id: 'LIST' }],
      }),
      createTopic: builder.mutation<ICreateTopicResponse, ICreateTopicRequest>({
        query: ({ token, userId, title, description, tags }) => {
          return {
            url: '/topics',
            method: 'POST',
            body: { userId, title, description, tags },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        invalidatesTags: [{ type: 'Topic', id: 'LIST' }],
      }),
    };
  },
});
export const { useCreateTopicMutation, useLazyFetchTopicsQuery } = topicsApi;
export { topicsApi };
