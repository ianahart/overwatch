import { createApi } from '@reduxjs/toolkit/query/react';
import {
  IFetchLatestWorkSpaceRequest,
  IFetchLatestWorkSpaceResponse,
  ICreateWorkSpaceResponse,
  ICreateWorkSpaceRequest,
  IFetchWorkSpacesResponse,
  IFetchWorkSpacesRequest,
  IUpdateWorkSpaceResponse,
  IUpdateWorkSpaceRequest,
  IDeleteWorkSpaceRequest,
  IDeleteWorkSpaceResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const workSpacesApi = createApi({
  reducerPath: 'workSpaces',
  tagTypes: ['WorkSpace'],
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      deleteWorkSpace: builder.mutation<IDeleteWorkSpaceResponse, IDeleteWorkSpaceRequest>({
        query: ({ id, token }) => ({
          url: `workspaces/${id}`,
          method: 'DELETE',
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }),
        //@ts-ignore
        invalidatesTags: (_, error, { id }) => [
          { type: 'WorkSpace', id },
          { type: 'WorkSpace', id: 'LIST' },
        ],
      }),

      editWorkSpace: builder.mutation<IUpdateWorkSpaceResponse, IUpdateWorkSpaceRequest>({
        query: ({ token, userId, id, workSpace }) => {
          return {
            url: `/workspaces/${id}`,
            method: 'PATCH',
            body: {
              title: workSpace.title,
              backgroundColor: workSpace.backgroundColor,
              userId,
            },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        invalidatesTags: (_, error, { id }) => {
          console.log(error);
          return [
            { type: 'WorkSpace', id: id },
            { type: 'WorkSpace', id: 'LIST' },
          ];
        },
      }),
      fetchLatestWorkspace: builder.query<IFetchLatestWorkSpaceResponse, IFetchLatestWorkSpaceRequest>({
        query: ({ userId, token }) => {
          if (userId === 0 || userId === null) {
            return '';
          }
          return {
            url: `/workspaces/latest?userId=${userId}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
      fetchWorkspaces: builder.query<IFetchWorkSpacesResponse, IFetchWorkSpacesRequest>({
        query: ({ userId, token, page, pageSize, direction }) => {
          if (userId === 0 || userId === null) {
            return '';
          }
          return {
            url: `/workspaces?userId=${userId}&page=${page}&pageSize=${pageSize}&direction=${direction}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        //@ts-ignore
        providesTags: (result, error, arg) =>
          result
            ? [...result.data.items.map(({ id }) => ({ type: 'WorkSpace', id })), { type: 'WorkSpace', id: 'LIST' }]
            : [{ type: 'WorkSpace', id: 'LIST' }],
      }),

      createWorkSpace: builder.mutation<ICreateWorkSpaceResponse, ICreateWorkSpaceRequest>({
        query: ({ userId, token, workSpace }) => {
          return {
            url: `/workspaces`,
            method: 'POST',
            headers: {
              Authorization: `Bearer ${token}`,
            },
            body: {
              title: workSpace.title,
              backgroundColor: workSpace.backgroundColor,
              userId,
            },
          };
        },
      }),
    };
  },
});

export const {
  useFetchLatestWorkspaceQuery,
  useDeleteWorkSpaceMutation,
  useEditWorkSpaceMutation,
  useCreateWorkSpaceMutation,
  useFetchWorkspacesQuery,
  useLazyFetchWorkspacesQuery,
} = workSpacesApi;
export { workSpacesApi };
