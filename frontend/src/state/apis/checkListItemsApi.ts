import { createApi } from '@reduxjs/toolkit/query/react';
import { ICreateCheckListItemRequest, ICreateCheckListItemResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const checkListItemsApi = createApi({
  reducerPath: 'checkListItems',
  tagTypes: ['CheckListItem'],
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
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

export const { useCreateCheckListItemMutation } = checkListItemsApi;
export { checkListItemsApi };
