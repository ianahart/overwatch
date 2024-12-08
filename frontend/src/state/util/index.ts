import { FetchArgs, FetchBaseQueryError, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { clearUser, updateTokens } from '../store';
import { baseURL, retrieveTokens } from '../../util';

import { BaseQueryFn } from '@reduxjs/toolkit/dist/query/baseQueryTypes';
import { ITokens } from '../../interfaces';

export const baseQuery = fetchBaseQuery({
  baseUrl: baseURL,
  credentials: 'include',
});
import { Mutex } from 'async-mutex';

const mutex = new Mutex();
export const baseQueryWithReauth: BaseQueryFn<string | FetchArgs, unknown, FetchBaseQueryError> = async (
  args,
  api,
  extraOptions
) => {
  await mutex.waitForUnlock();
  let result = await baseQuery(args, api, extraOptions);
  if (result.error && result.error.status === 403) {
    if (!mutex.isLocked()) {
      const release = await mutex.acquire();
      try {
        const refreshToken = retrieveTokens().refreshToken;

        if (!refreshToken) {
          throw new Error('No refresh token available');
        }
        const refreshResult = await baseQuery(
          {
            url: '/auth/refresh',
            method: 'POST',
            body: { refreshToken },
            headers: {},
          },
          api,
          extraOptions
        );
        if (refreshResult.data) {
          const { token, refreshToken } = refreshResult.data as ITokens;
          localStorage.setItem('tokens', JSON.stringify({ token, refreshToken }));
          api.dispatch(updateTokens({ token, refreshToken }));

          result = await baseQuery(
            {
              //@ts-ignore
              ...args,
              headers: {
                //@ts-ignore
                ...(args.headers || {}), // Ensure args.headers is an object
                Authorization: `Bearer ${token}`,
              },
            },
            api,
            extraOptions
          );
        } else {
          api.dispatch(clearUser());
        }
      } finally {
        release();
      }
    } else {
      await mutex.waitForUnlock();
      result = await baseQuery(args, api, extraOptions);
    }
  }
  return result;
};
