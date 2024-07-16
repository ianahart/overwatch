import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateUserRepositoryRequest,
  ICreateUserRepositoryResponse,
  IDeleteUserRepositoryResponse,
  IDeleteUserRepositoryRequest,
  IFetchDistinctRepositoryLanguagesRequest,
  IFetchDistinctRepositoryLanguagesResponse,
  IFetchRepositoriesRequest,
  IFetchRepositoriesResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const repositoriesApi = createApi({
  reducerPath: 'repository',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['Repository'],
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
        //@ts-ignore
        providesTags: (result, error, arg) =>
          result
            ? [...result.data.items.map(({ id }) => ({ type: 'Repository', id })), { type: 'Repository', id: 'LIST' }]
            : [{ type: 'Repository', id: 'LIST' }],
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
      deleteUserRepository: builder.mutation<IDeleteUserRepositoryResponse, IDeleteUserRepositoryRequest>({
        query: ({ repositoryId, token }) => {
          return {
            url: `/repositories/${repositoryId}`,
            method: 'DELETE',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        //@ts-ignore
        invalidatesTags: (_, error, { repositoryId }) => [
          { type: 'Repository', id: repositoryId },
          { type: 'Repository', id: 'LIST' },
        ],
      }),
    };
  },
});

export const {
  useDeleteUserRepositoryMutation,
  useFetchRepositoriesQuery,
  useLazyFetchRepositoriesQuery,
  useCreateUserRepositoryMutation,
  useFetchDistinctRepositoryLanguagesQuery,
} = repositoriesApi;
export { repositoriesApi };
