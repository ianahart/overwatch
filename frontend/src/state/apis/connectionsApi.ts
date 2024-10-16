import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateConnectionRequest,
  ICreateConnectionResponse,
  IDeleteConnectionRequest,
  IDeleteConnectionResponse,
  IFetchConnectionsRequest,
  IFetchConnectionsResponse,
  IFetchSearchConnectionsRequest,
  IFetchSearchConnectionsResposne,
  IVerifyConnectionRequest,
  IVerifyConnectionResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const connectionsApi = createApi({
  reducerPath: 'connections',
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      fetchConnections: builder.query<IFetchConnectionsResponse, IFetchConnectionsRequest>({
        query: ({ userId, token, page, pageSize, direction, override = 'false' }) => {
          if (userId === 0 || !token) {
            return '';
          }
          return {
            url: `/connections?userId=${userId}&page=${page}&pageSize=${pageSize}&direction=${direction}&override=${override}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
      fetchSearchConnections: builder.query<IFetchSearchConnectionsResposne, IFetchSearchConnectionsRequest>({
        query: ({ token, page, pageSize, direction, query }) => {
          if (!token) {
            return '';
          }
          return {
            url: `/connections/search?query=${query}&page=${page}&pageSize=${pageSize}&direction=${direction}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
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
      deleteConnection: builder.mutation<IDeleteConnectionResponse, IDeleteConnectionRequest>({
        query: ({ connectionId, token }) => {
          return {
            url: `/connections/${connectionId}`,
            method: 'DELETE',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
    };
  },
});

export const {
  useLazyFetchSearchConnectionsQuery,
  useCreateConnectionMutation,
  useVerifyConnectionQuery,
  useDeleteConnectionMutation,
  useFetchConnectionsQuery,
  useLazyFetchConnectionsQuery,
} = connectionsApi;
export { connectionsApi };
