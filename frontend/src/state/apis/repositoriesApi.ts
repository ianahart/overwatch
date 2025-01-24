import { createApi } from '@reduxjs/toolkit/query/react';
import {
  IFetchSearchRepositoryRequest,
  IFetchSearchRepositoryResponse,
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
  IUpdateRepositoryReviewStartTimeResponse,
  IUpdateRepositoryReviewStartTimeRequest,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const repositoriesApi = createApi({
  reducerPath: 'repository',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['Repository'],
  endpoints(builder) {
    return {
      searchRepository: builder.query<IFetchSearchRepositoryResponse, IFetchSearchRepositoryRequest>({
        query: ({ token, query, repoName, repositoryPage, githubId }) => {
          if (!token || !githubId) {
            return '';
          }
          return {
            url: `/repositories/search?githubId=${githubId}&page=${repositoryPage}&size=30&query=${encodeURIComponent(
              `repo:${repoName} ${query}`
            )}&repoName=${repoName}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
      updateRepositoryReviewStartTime: builder.mutation<
        IUpdateRepositoryReviewStartTimeResponse,
        IUpdateRepositoryReviewStartTimeRequest
      >({
        query: ({ reviewStartTime, repositoryId, status, token }) => {
          if (repositoryId == undefined || token === null || token === '') {
            return '';
          }
          return {
            url: `/repositories/${repositoryId}/starttime`,
            method: 'PATCH',
            headers: {
              Authorization: `Bearer ${token}`,
            },
            body: { reviewStartTime, status },
          };
        },
      }),
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
        query: ({ token, githubId, repoName, owner, path }) => {
          return {
            url: `/repositories/file`,
            method: 'POST',
            headers: {
              Authorization: `Bearer ${token}`,
            },
            body: { repoName, owner, path, githubId },
          };
        },
      }),
      fetchRepository: builder.query<IFetchRepositoryResponse, IFetchRepositoryRequest>({
        query: ({ token, repositoryId, githubId, repositoryPage }) => {
          return {
            url: `/repositories/${repositoryId}?page=${repositoryPage}&size=50&githubId=${githubId}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
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
          if (token === '' || !token) {
            return '';
          }
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
        // @ts-ignore
        invalidatesTags: [{ type: 'Repository', id: 'LIST' }],
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
  useLazySearchRepositoryQuery,
  useUpdateRepositoryReviewStartTimeMutation,
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
