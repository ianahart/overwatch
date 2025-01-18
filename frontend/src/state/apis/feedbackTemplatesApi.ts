import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateFeedbackTemplateRequest,
  ICreateFeedbackTemplateResponse,
  IGetFeedbackTemplateRequest,
  IGetFeedbackTemplateResponse,
  IGetAllFeedbackTemplateRequest,
  IGetAllFeedbackTemplateResponse,
  IDeleteFeedbackTemplateRequest,
  IDeleteFeedbackTemplateResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const feedbackTemplatesApi = createApi({
  reducerPath: 'feedbackTemplates',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['FeedbackTemplate'],
  endpoints(builder) {
    return {
      deleteFeedbackTemplate: builder.mutation<IDeleteFeedbackTemplateResponse, IDeleteFeedbackTemplateRequest>({
        query: ({ feedbackTemplateId, token }) => ({
          url: `feedback-templates/${feedbackTemplateId}`,
          method: 'DELETE',
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }),
        //@ts-ignore
        invalidatesTags: (_, error, { feedbackTemplateId }) => [
          { type: 'FeedbackTemplate', id: feedbackTemplateId },
          { type: 'FeedbackTemplate', id: 'LIST' },
        ],
      }),
      fetchFeedbackTemplate: builder.query<IGetFeedbackTemplateResponse, IGetFeedbackTemplateRequest>({
        query: ({ token, feedbackTemplateId }) => {
          return {
            url: `/feedback-templates/${feedbackTemplateId}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
      fetchFeedbackTemplates: builder.query<IGetAllFeedbackTemplateResponse, IGetAllFeedbackTemplateRequest>({
        query: ({ token }) => {
          return {
            url: `/feedback-templates`,
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
                ...result.data.map(({ id }) => ({ type: 'FeedbackTemplate', id })),
                { type: 'FeedbackTemplate', id: 'LIST' },
              ]
            : [{ type: 'FeedbackTemplate', id: 'LIST' }],
      }),

      createFeedbackTemplate: builder.mutation<ICreateFeedbackTemplateResponse, ICreateFeedbackTemplateRequest>({
        query: ({ token, userId, feedback }) => {
          return {
            url: `/feedback-templates`,
            method: 'POST',
            body: { token, userId, feedback },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        invalidatesTags: [{ type: 'FeedbackTemplate', id: 'LIST' }],
      }),
    };
  },
});
export const {
  useCreateFeedbackTemplateMutation,
  useDeleteFeedbackTemplateMutation,
  useLazyFetchFeedbackTemplateQuery,
  useFetchFeedbackTemplatesQuery,
} = feedbackTemplatesApi;
export { feedbackTemplatesApi };
