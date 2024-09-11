import { createApi } from '@reduxjs/toolkit/query/react';
import { ICreateCheckListRequest, ICreateCheckListResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const checkListsApi = createApi({
  reducerPath: 'checkLists',
  tagTypes: ['CheckList'],
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
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
        invalidatesTags: (result, error, { cardId }) => {
          console.log(result, error, cardId);
          return [{ type: 'CheckList', id: 'LIST' }];
        },
      }),
    };
  },
});

export const { useCreateCheckListMutation } = checkListsApi;
export { checkListsApi };
