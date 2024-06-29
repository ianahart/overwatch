import { createApi } from '@reduxjs/toolkit/query/react';
import { IFetchChatMessagesRequest, IFetchChatMessagesResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const chatMessagesApi = createApi({
  reducerPath: 'chatMessages',
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      fetchChatMessages: builder.query<IFetchChatMessagesResponse, IFetchChatMessagesRequest>({
        query: ({ connectionId, token }) => {
          if (connectionId === 0 || !token) {
            return '';
          }
          return {
            url: `/chat-messages?connectionId=${connectionId}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
    };
  },
});

export const { useLazyFetchChatMessagesQuery } = chatMessagesApi;
export { chatMessagesApi };
