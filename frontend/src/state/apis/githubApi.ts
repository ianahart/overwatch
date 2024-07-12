import { createApi } from '@reduxjs/toolkit/query/react';
import { IFetchGitHubTokenRequest, IFetchGitHubTokenResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const githubApi = createApi({
  reducerPath: 'github',
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      fetchGitHubAccessToken: builder.query<IFetchGitHubTokenResponse, IFetchGitHubTokenRequest>({
        query: ({ code, token }) => {
          return {
            url: `/github/auth?code=${code}`,
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

export const { useLazyFetchGitHubAccessTokenQuery } = githubApi;
export { githubApi };
