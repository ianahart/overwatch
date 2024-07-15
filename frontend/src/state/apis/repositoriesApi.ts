import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateUserRepositoryRequest,
  ICreateUserRepositoryResponse,
  IFetchDistinctRepositoryLanguagesRequest,
  IFetchDistinctRepositoryLanguagesResponse,
  IFetchRepositoriesRequest,
  IFetchRepositoriesResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const repositoriesApi = createApi({
  reducerPath: 'repository',
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      fetchRepositories: builder.query<IFetchRepositoriesResponse, IFetchRepositoriesRequest>({
        query: ({ token, page, pageSize, direction, sortFilter, statusFilter, languageFilter }) => {
          return {
            url: `/repositories?page=${page}&pageSize=${pageSize}&direction=${direction}&sort=${sortFilter}&status=${statusFilter}&language=${languageFilter}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),

      fetchDistinctRepositoryLanguages: builder.query<
        IFetchDistinctRepositoryLanguagesResponse,
        IFetchDistinctRepositoryLanguagesRequest
      >({
        query: ({ token }) => {
          return {
            url: '/repositories/languages',
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
      createUserRepository: builder.mutation<ICreateUserRepositoryResponse, ICreateUserRepositoryRequest>({
        query: ({ payload, token }) => {
          return {
            url: `/repositories/user`,
            method: 'POST',
            headers: {
              Authorization: `Bearer ${token}`,
            },
            body: payload,
          };
        },
      }),
    };
  },
});

export const {
  useFetchRepositoriesQuery,
  useLazyFetchRepositoriesQuery,
  useCreateUserRepositoryMutation,
  useFetchDistinctRepositoryLanguagesQuery,
} = repositoriesApi;
export { repositoriesApi };
