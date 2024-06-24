import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateConnectionRequest,
  ICreateConnectionResponse,
  IVerifyConnectionRequest,
  IVerifyConnectionResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const connectionsApi = createApi({
  reducerPath: 'connections',
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      verifyConnection: builder.query<IVerifyConnectionResponse, IVerifyConnectionRequest>({
        query: ({ senderId, receiverId, token }) => {
          if (senderId === null || senderId === 0 || receiverId === 0 || receiverId === null) {
            return '';
          }
          return {
            url: `/connections/verify?senderId=${senderId}&receiverId=${receiverId}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
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

export const { useCreateConnectionMutation, useVerifyConnectionQuery } = connectionsApi;
export { connectionsApi };
