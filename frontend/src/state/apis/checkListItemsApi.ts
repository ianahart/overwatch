import { createApi } from '@reduxjs/toolkit/query/react';
import {
  IDeleteCheckListItemRequest,
  IDeleteCheckListItemResponse,
  ICreateCheckListItemRequest,
  ICreateCheckListItemResponse,
  IUpdateCheckListItemRequest,
  IUpdateCheckListItemResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const checkListItemsApi = createApi({
  reducerPath: 'checkListItems',
  tagTypes: ['CheckListItem'],
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      updateCheckListItem: builder.mutation<IUpdateCheckListItemResponse, IUpdateCheckListItemRequest>({
        query: ({ data, token }) => {
          return {
            url: `/checklist-items/${data.id}`,
            method: 'PUT',
            headers: {
              Authorization: `Bearer ${token}`,
            },
            body: data,
          };
        },
      }),
      deleteCheckListItem: builder.mutation<IDeleteCheckListItemResponse, IDeleteCheckListItemRequest>({
        query: ({ token, id }) => {
          return {
            url: `/checklist-items/${id}`,
            method: 'DELETE',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
      createCheckListItem: builder.mutation<ICreateCheckListItemResponse, ICreateCheckListItemRequest>({
        query: ({ checkListId, userId, title, token }) => {
          return {
            url: `/checklist-items`,
            method: 'POST',
            headers: {
              Authorization: `Bearer ${token}`,
            },
            body: {
              checkListId,
              userId,
              title,
            },
          };
        },
        invalidatesTags: (result, error, { checkListId }) => {
          console.log(result, error, checkListId);
          return [{ type: 'CheckListItem', id: 'LIST' }];
        },
      }),
    };
  },
});

export const { useDeleteCheckListItemMutation, useCreateCheckListItemMutation, useUpdateCheckListItemMutation } =
  checkListItemsApi;
export { checkListItemsApi };
