import { createApi } from '@reduxjs/toolkit/query/react';
import {
  IFetchPinnedConnectionsResponse,
  IFetchPinnedConnectionsRequest,
  ICreatePinnedConnectionRequest,
  ICreatePinnedConnectionResponse,
  IDeletePinnedConnectionResponse,
  IDeletePinnedConnectionRequest,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const pinnedConnectionsApi = createApi({
  reducerPath: 'pinnedConnections',
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      fetchPinnedConnections: builder.query<IFetchPinnedConnectionsResponse, IFetchPinnedConnectionsRequest>({
        query: ({ userId, token }) => {
          if (userId === 0 || !token) {
            return '';
          }
          return {
            url: `/connection-pins?ownerId=${userId}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
      createPinnedConnection: builder.mutation<ICreatePinnedConnectionResponse, ICreatePinnedConnectionRequest>({
        query: ({ pinnedId, connectionId, ownerId, token }) => {
          return {
            url: `/connection-pins`,
            method: 'POST',
            body: {
              pinnedId,
              ownerId,
              connectionId,
            },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
      deletePinnedConnection: builder.mutation<IDeletePinnedConnectionResponse, IDeletePinnedConnectionRequest>({
        query: ({ connectionPinId, token }) => {
          if (!token) {
            return '';
          }
          return {
            url: `/connection-pins/${connectionPinId}`,
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
  useDeletePinnedConnectionMutation,
  useCreatePinnedConnectionMutation,
  useFetchPinnedConnectionsQuery,
  useLazyFetchPinnedConnectionsQuery,
} = pinnedConnectionsApi;
export { pinnedConnectionsApi };
