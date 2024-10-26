import { createApi } from '@reduxjs/toolkit/query/react';
import { ICreateReportCommentRequest, ICreateReportCommentResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const reportCommentsApi = createApi({
  reducerPath: 'reportComments',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['ReportComment'],
  endpoints(builder) {
    return {
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
    };
  },
});
export const { useCreateReportCommentMutation } = reportCommentsApi;
export { reportCommentsApi };
