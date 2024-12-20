import { createApi } from '@reduxjs/toolkit/query/react';
import { IGetAllReviewerBadgesRequest, IGetAllReviewerBadgesResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const reviewerBadgesApi = createApi({
  reducerPath: 'reviewerBadges',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['ReviewerBadge'],
  endpoints(builder) {
    return {
      fetchReviewerBadges: builder.query<IGetAllReviewerBadgesResponse, IGetAllReviewerBadgesRequest>({
        query: ({ reviewerId, token, page, pageSize, direction }) => {
          if (reviewerId === 0 || reviewerId === null) {
            return '';
          }
          return {
            url: `/reviewer-badges?reviewerId=${reviewerId}&page=${page}&pageSize=${pageSize}&direction=${direction}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        //@ts-ignore
        providesTags: (result, error, arg) =>
          result
            ? [
                ...result.data.items.map(({ id }) => ({ type: 'ReviewerBadge', id })),
                { type: 'ReviewerBadge', id: 'LIST' },
              ]
            : [{ type: 'ReviewerBadge', id: 'LIST' }],
      }),
    };
  },
});
export const { useLazyFetchReviewerBadgesQuery } = reviewerBadgesApi;
export { reviewerBadgesApi };
