import { createApi } from '@reduxjs/toolkit/query/react';
import { ICreateBadgeRequest, ICreateBadgeResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const badgesApi = createApi({
  reducerPath: 'badges',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['Badge'],
  endpoints(builder) {
    return {
      createBadge: builder.mutation<ICreateBadgeResponse, ICreateBadgeRequest>({
        query: ({ token, body }) => {
          return {
            url: `/admin/badges`,
            method: 'POST',
            body,
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        invalidatesTags: [{ type: 'Badge', id: 'LIST' }],
      }),
    };
  },
});
export const { useCreateBadgeMutation } = badgesApi;
export { badgesApi };
