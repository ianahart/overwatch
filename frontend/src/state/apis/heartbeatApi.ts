import { createApi } from '@reduxjs/toolkit/query/react';
import { IHeartBeatResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const heartbeatApi = createApi({
  reducerPath: 'heartbeat',
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      fetchHeartBeat: builder.query<IHeartBeatResponse, string>({
        query: (token) => {
          return {
            url: '/heartbeat',
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

export const { useLazyFetchHeartBeatQuery, useFetchHeartBeatQuery } = heartbeatApi;
export { heartbeatApi };
