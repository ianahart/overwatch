import { createApi } from '@reduxjs/toolkit/query/react';
import { ISyncUserResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const usersApi = createApi({
  reducerPath: 'users',
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      syncUser: builder.query<ISyncUserResponse, string>({
        query: (token) => {
          return {
            url: '/users/sync',
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

export const { useSyncUserQuery } = usersApi;
export { usersApi };
