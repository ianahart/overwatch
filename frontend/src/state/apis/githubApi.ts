import { createApi } from '@reduxjs/toolkit/query/react';
import {
  IFetchGitHubTokenRequest,
  IFetchGitHubTokenResponse,
  IFetchGitHubUserReposRequest,
  IFetchGitHubUserReposResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const githubApi = createApi({
  reducerPath: 'github',
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      fetchGitHubUserRepos: builder.query<IFetchGitHubUserReposResponse, IFetchGitHubUserReposRequest>({
        query: ({ githubId, token, page }) => {
          return {
            url: `/github/user/repos?page=${page}&githubId=${githubId}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
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

export const { useLazyFetchGitHubAccessTokenQuery, useFetchGitHubUserReposQuery, useLazyFetchGitHubUserReposQuery } =
  githubApi;
export { githubApi };
