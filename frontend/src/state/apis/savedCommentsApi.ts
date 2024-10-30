import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateSaveCommentRequest,
  ICreateSaveCommentResponse,
  IDeleteSaveCommentRequest,
  IDeleteSaveCommentResponse,
  IGetSaveCommentsRequest,
  IGetSaveCommentsResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const saveCommentsApi = createApi({
  reducerPath: 'saveComments',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['SaveComment'],
  endpoints(builder) {
    return {
      fetchSaveComments: builder.query<IGetSaveCommentsResponse, IGetSaveCommentsRequest>({
        query: ({ token, page, pageSize, direction, userId }) => {
          if (!token || !userId) {
            return '';
          }
          return {
            url: `/save-comments?userId=${userId}&page=${page}&pageSize=${pageSize}&direction=${direction}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        //@ts-ignore
        providesTags: (result, error, arg) =>
          result
            ? [...result.data.items.map(({ id }) => ({ type: 'SaveComment', id })), { type: 'SaveComment', id: 'LIST' }]
            : [{ type: 'SaveComment', id: 'LIST' }],
      }),

      createSaveComment: builder.mutation<ICreateSaveCommentResponse, ICreateSaveCommentRequest>({
        query: ({ token, userId, commentId }) => {
          return {
            url: '/save-comments',
            method: 'POST',
            body: { userId, commentId },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        invalidatesTags: [{ type: 'SaveComment', id: 'LIST' }],
      }),
      deleteSaveComment: builder.mutation<IDeleteSaveCommentResponse, IDeleteSaveCommentRequest>({
        query: ({ token, saveCommentId }) => {
          return {
            url: `/save-comments/${saveCommentId}`,
            method: 'DELETE',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        invalidatesTags: [{ type: 'SaveComment', id: 'LIST' }],
      }),
    };
  },
});
export const { useLazyFetchSaveCommentsQuery, useCreateSaveCommentMutation, useDeleteSaveCommentMutation } =
  saveCommentsApi;
export { saveCommentsApi };
