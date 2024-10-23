import { createApi } from '@reduxjs/toolkit/query/react';
import { ICreateTopicRequest, ICreateTopicResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const topicsApi = createApi({
  reducerPath: 'topics',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['Topic'],
  endpoints(builder) {
    return {
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
export const { useCreateTopicMutation } = topicsApi;
export { topicsApi };
