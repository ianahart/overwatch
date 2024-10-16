import { createApi } from '@reduxjs/toolkit/query/react';
import { ICreateBlockedUserRequest, ICreateBlockedUserResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const blockedUsersApi = createApi({
  reducerPath: 'blockedUsers',
  tagTypes: ['BlockedUser'],
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      createBlockedUser: builder.mutation<ICreateBlockedUserResponse, ICreateBlockedUserRequest>({
        query: ({ blockedUserId, blockerUserId, token }) => {
          return {
            url: `/block-users`,
            method: 'POST',
            headers: {
              Authorization: `Bearer ${token}`,
            },
            body: {
              blockedUserId,
              blockerUserId,
            },
          };
        },
        invalidatesTags: (result, error) => {
          console.log(result, error);
          return [{ type: 'BlockedUser', id: 'LIST' }];
        },
      }),
      //    fetchActiveLabels: builder.query<IFetchActiveLabelsResponse, IFetchActiveLabelsRequest>({
      //      query: ({ token, todoCardId }) => {
      //        if (todoCardId === 0 || todoCardId === null) {
      //          return '';
      //        }
      //        return {
      //          url: `/active-labels?todoCardId=${todoCardId}`,
      //          method: 'GET',
      //          headers: {
      //            Authorization: `Bearer ${token}`,
      //          },
      //        };
      //      },
      //      //@ts-ignore
      //      providesTags: (result, error, arg) =>
      //        result
      //          ? [...result.data.map(({ id }) => ({ type: 'BlockedUser', id })), { type: 'BlockedUser', id: 'LIST' }]
      //          : [{ type: 'BlockedUser', id: 'LIST' }],
      //    }),
    };
  },
});

export const { useCreateBlockedUserMutation } = blockedUsersApi;
export { blockedUsersApi };
