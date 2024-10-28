import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateTopicRequest,
  ICreateTopicResponse,
  IGetAllTopicsRequest,
  IGetAllTopicsResponse,
  IGetTopicRequest,
  IGetTopicResponse,
  IGetTopicsRequest,
  IGetTopicsResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const topicsApi = createApi({
  reducerPath: 'topics',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['Topic'],
  endpoints(builder) {
    return {
      fetchTopic: builder.query<IGetTopicResponse, IGetTopicRequest>({
        query: ({ topicId }) => {
          if (topicId === 0 || topicId === undefined) {
            return '';
          }
          return {
            url: `/topics/${topicId}`,
            method: 'GET',
            headers: {},
          };
        },
        providesTags: (result, _, arg) => (result ? [{ type: 'Topic', id: arg.topicId }] : []),
      }),

      searchTopics: builder.query<IGetTopicsResponse, IGetTopicsRequest>({
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
      fetchTopics: builder.query<IGetAllTopicsResponse, IGetAllTopicsRequest>({
        query: ({ page, pageSize, direction }) => {
          return {
            url: `/topics?page=${page}&pageSize=${pageSize}&direction=${direction}`,
            method: 'GET',
            headers: {},
          };
        },
        //@ts-ignore
        providesTags: (result, error, arg) =>
          result
            ? [...result.data.items.map(({ id }) => ({ type: 'Topic', id })), { type: 'Topic', id: 'LIST' }]
            : [{ type: 'Topic', id: 'LIST' }],
      }),
    };
  },
});
export const {
  useLazyFetchTopicsQuery,
  useFetchTopicsQuery,
  useCreateTopicMutation,
  useLazySearchTopicsQuery,
  useFetchTopicQuery,
} = topicsApi;
export { topicsApi };
