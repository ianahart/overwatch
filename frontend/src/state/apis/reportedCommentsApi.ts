import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateReportCommentRequest,
  ICreateReportCommentResponse,
  IDeleteReportCommentRequest,
  IDeleteReportCommentResponse,
  IGetAllReportCommentRequest,
  IGetAllReportCommentResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const reportCommentsApi = createApi({
  reducerPath: 'reportComments',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['ReportComment'],
  endpoints(builder) {
    return {
      fetchReportComments: builder.query<IGetAllReportCommentResponse, IGetAllReportCommentRequest>({
        query: ({ token, page, pageSize, direction }) => {
          if (!token) {
            return '';
          }
          return {
            url: `/admin/report-comments?page=${page}&pageSize=${pageSize}&direction=${direction}`,
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
                ...result.data.items.map(({ id }) => ({ type: 'ReportComment', id })),
                { type: 'ReportComment', id: 'LIST' },
              ]
            : [{ type: 'ReportCommentt', id: 'LIST' }],
      }),

      createReportComment: builder.mutation<ICreateReportCommentResponse, ICreateReportCommentRequest>({
        query: ({ token, userId, commentId, reason, details }) => {
          return {
            url: '/report-comments',
            method: 'POST',
            body: { userId, commentId, reason, details },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        invalidatesTags: [{ type: 'ReportComment', id: 'LIST' }],
      }),
      deleteReportComment: builder.mutation<IDeleteReportCommentResponse, IDeleteReportCommentRequest>({
        query: ({ id, token }) => {
          return {
            url: `/admin/report-comments/${id}`,
            method: 'DELETE',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        invalidatesTags: [{ type: 'ReportComment', id: 'LIST' }],
      }),
    };
  },
});
export const { useDeleteReportCommentMutation, useCreateReportCommentMutation, useLazyFetchReportCommentsQuery } =
  reportCommentsApi;
export { reportCommentsApi };
