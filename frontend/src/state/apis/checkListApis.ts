import { createApi } from '@reduxjs/toolkit/query/react';
import {
  IDeleteCheckListRequest,
  IDeleteCheckListResponse,
  ICreateCheckListRequest,
  ICreateCheckListResponse,
  IFetchCheckListsRequest,
  IFetchCheckListsResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const checkListsApi = createApi({
  reducerPath: 'checkLists',
  tagTypes: ['CheckList'],
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      fetchCheckLists: builder.query<IFetchCheckListsResponse, IFetchCheckListsRequest>({
        query: ({ token, todoCardId }) => {
          if (todoCardId === 0 || todoCardId === null) {
            return '';
          }
          return {
            url: `/todo-cards/${todoCardId}/checklists`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        //@ts-ignore
        providesTags: (result, error, arg) =>
          result
            ? [...result.data.map(({ id }) => ({ type: 'CheckList', id })), { type: 'CheckList', id: 'LIST' }]
            : [{ type: 'CheckList', id: 'LIST' }],
      }),
      createCheckList: builder.mutation<ICreateCheckListResponse, ICreateCheckListRequest>({
        query: ({ todoCardId, userId, title, token }) => {
          return {
            url: `/checklists`,
            method: 'POST',
            headers: {
              Authorization: `Bearer ${token}`,
            },
            body: {
              todoCardId,
              userId,
              title,
            },
          };
        },
        invalidatesTags: (result, error, { todoCardId }) => {
          console.log(result, error, todoCardId);
          return [{ type: 'CheckList', id: 'LIST' }];
        },
      }),
      deleteCheckList: builder.mutation<IDeleteCheckListResponse, IDeleteCheckListRequest>({
        query: ({ id, token }) => ({
          url: `checklists/${id}`,
          method: 'DELETE',
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }),
        //@ts-ignore
        invalidatesTags: (_, error, { id }) => [
          { type: 'CheckList', id: id },
          { type: 'CheckList', id: 'LIST' },
        ],
      }),
    };
  },
});

export const { useDeleteCheckListMutation, useCreateCheckListMutation, useFetchCheckListsQuery } = checkListsApi;
export { checkListsApi };
