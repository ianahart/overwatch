import { FetchArgs, FetchBaseQueryError, FetchBaseQueryMeta, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { clearUser, updateTokens } from '../store';
import { baseURL, retrieveTokens } from '../../util';

import { MaybePromise } from '@reduxjs/toolkit/dist/query/tsHelpers';
import { BaseQueryFn, QueryReturnValue } from '@reduxjs/toolkit/dist/query/baseQueryTypes';
import { ITokens } from '../../interfaces';

export const baseQuery = fetchBaseQuery({
  baseUrl: baseURL,
  credentials: 'include',
});

let isRefreshing = false;
let failedRequestsQueue: { args: FetchArgs; api: any; extraOptions: any }[] = [];
export const baseQueryWithReauth: BaseQueryFn<string | FetchArgs, unknown, FetchBaseQueryError> = async (
  args,
  api,
  extraOptions
) => {
  let result: MaybePromise<QueryReturnValue<unknown, FetchBaseQueryError, FetchBaseQueryMeta>> = await baseQuery(
    args,
    api,
    extraOptions
  );

  const refreshToken = retrieveTokens().refreshToken;
  if ((result?.error as any)?.status === 403) {
    if (!isRefreshing) {
      isRefreshing = true;
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

        for (const request of failedRequestsQueue) {
          const { args, api, extraOptions } = request;
          await baseQuery(args, api, extraOptions);
        }
      } else {
        api.dispatch(clearUser());
      }
      isRefreshing = false;
      failedRequestsQueue = []; // Clear the failed requests queue
    } else {
      // @ts-ignoree
      failedRequestsQueue.push({ args, api, extraOptions });
      result = new Promise(() => {});
    }
  }
  return result;
};
