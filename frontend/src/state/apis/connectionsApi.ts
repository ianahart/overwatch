import { createApi } from '@reduxjs/toolkit/query/react';
import { ICreateConnectionRequest, ICreateConnectionResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const connectionsApi = createApi({
  reducerPath: 'connections',
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      createConnection: builder.mutation<ICreateConnectionResponse, ICreateConnectionRequest>({
        query: ({ senderId, receiverId, token }) => {
          return {
            url: `/connections`,
            method: 'POST',
            body: {
              receiverId,
              senderId,
            },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
    };
  },
});

export const { useCreateConnectionMutation } = connectionsApi;
export { connectionsApi };
