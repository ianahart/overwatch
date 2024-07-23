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
  IFetchUserCommentRepositoryRequest,
  IFetchUserCommentRepositoryResponse,
  IUpdateRepositoryCommentResponse,
  IUpdateRepositoryCommentRequest,
  IFetchRepositoryRequest,
  IFetchRepositoryResponse,
  ICreateRepositoryFileResponse,
  ICreateRepositoryFileRequest,
  IUpdateRepositoryResponse,
  IUpdateRepositoryRequest,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const repositoriesApi = createApi({
  reducerPath: 'repository',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['Repository'],
  endpoints(builder) {
    return {
      updateRepositoryReview: builder.mutation<IUpdateRepositoryResponse, IUpdateRepositoryRequest>({
        query: ({ feedback, repositoryId, status, token }) => {
          return {
            url: `/repositories/${repositoryId}`,
            method: 'PATCH',
            headers: {
              Authorization: `Bearer ${token}`,
            },
            body: { feedback, status },
          };
        },
      }),
      createRepositoryFile: builder.mutation<ICreateRepositoryFileResponse, ICreateRepositoryFileRequest>({
        query: ({ token, accessToken, repoName, owner, path }) => {
          return {
            url: `/repositories/file`,
            method: 'POST',
            headers: {
              Authorization: `Bearer ${token}`,
            },
            body: { repoName, owner, path, accessToken },
          };
        },
      }),
      fetchRepository: builder.query<IFetchRepositoryResponse, IFetchRepositoryRequest>({
        query: ({ token, repositoryId, accessToken, repositoryPage }) => {
          return {
            url: `/repositories/${repositoryId}?page=${repositoryPage}&size=50`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
              'GitHub-Token': accessToken,
            },
          };
        },
      }),

      fetchUserCommentRepository: builder.query<
        IFetchUserCommentRepositoryResponse,
        IFetchUserCommentRepositoryRequest
      >({
        query: ({ token, repositoryId }) => {
          return {
            url: `/repositories/${repositoryId}/comment`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        providesTags: (_, error, { repositoryId }) => {
          console.log(error);
          return [{ type: 'Repository', id: repositoryId }];
        },
        //@ts-ignore
        invalidatesTags: (_, error, { repositoryId }) => [
          { type: 'Repository', id: repositoryId },
          { type: 'Repository', id: 'LIST' },
        ],
      }),
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
      updateRepositoryComment: builder.mutation<IUpdateRepositoryCommentResponse, IUpdateRepositoryCommentRequest>({
        query: ({ repositoryId, token, comment }) => {
          return {
            url: `/repositories/${repositoryId}/comment`,
            method: 'PATCH',
            headers: {
              Authorization: `Bearer ${token}`,
            },
            body: {
              comment,
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
  useUpdateRepositoryReviewMutation,
  useCreateRepositoryFileMutation,
  useLazyFetchRepositoryQuery,
  useFetchRepositoryQuery,
  useUpdateRepositoryCommentMutation,
  useFetchUserCommentRepositoryQuery,
  useDeleteUserRepositoryMutation,
  useFetchRepositoriesQuery,
  useLazyFetchRepositoriesQuery,
  useCreateUserRepositoryMutation,
  useFetchDistinctRepositoryLanguagesQuery,
} = repositoriesApi;
export { repositoriesApi };
