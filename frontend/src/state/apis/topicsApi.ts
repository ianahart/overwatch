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
  IGetTopicsWithTagsRequest,
  IGetTopicsWithTagsResponse,
  IGetAllUserTopicsRequest,
  IGetAllUserTopicsResponse,
  IUpdateTopicResponse,
  IUpdateTopicRequest,
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

      updateTopic: builder.mutation<IUpdateTopicResponse, IUpdateTopicRequest>({
        query: ({ token, userId, topicId, description, tags }) => {
          return {
            url: `/topics/${topicId}`,
            method: 'PATCH',
            body: { userId, description, tags },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        invalidatesTags: [{ type: 'Topic', id: 'LIST' }],
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

      fetchTopicsWithTags: builder.query<IGetTopicsWithTagsResponse, IGetTopicsWithTagsRequest>({
        query: ({ query, page, pageSize, direction }) => {
          return {
            url: `/topics/tags?query=${query}&page=${page}&pageSize=${pageSize}&direction=${direction}`,
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
      fetchUserTopics: builder.query<IGetAllUserTopicsResponse, IGetAllUserTopicsRequest>({
        query: ({ page, pageSize, direction, userId, token }) => {
          return {
            url: `/topics/users/${userId}?page=${page}&pageSize=${pageSize}&direction=${direction}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
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
  useUpdateTopicMutation,
  useLazyFetchUserTopicsQuery,
  useLazyFetchTopicsWithTagsQuery,
  useLazyFetchTopicsQuery,
  useFetchTopicsQuery,
  useCreateTopicMutation,
  useLazySearchTopicsQuery,
  useFetchTopicQuery,
} = topicsApi;
export { topicsApi };
