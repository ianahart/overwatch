import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateSuggestionRequest,
  ICreateSuggestionResponse,
  IDeleteSuggestionRequest,
  IDeleteSuggestionResponse,
  IGetAllSuggestionsRequest,
  IGetAllSuggestionsResponse,
  IUpdateSuggestionRequest,
  IUpdateSuggestionResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const suggestionsApi = createApi({
  reducerPath: 'suggestions',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['Suggestion'],
  endpoints(builder) {
    return {
      fetchSuggestions: builder.query<IGetAllSuggestionsResponse, IGetAllSuggestionsRequest>({
        query: ({ feedbackStatus, token, page, pageSize, direction }) => {
          if (!token) {
            return '';
          }
          return {
            url: `/admin/suggestions?feedbackStatus=${feedbackStatus}&page=${page}&pageSize=${pageSize}&direction=${direction}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
      deleteSuggestion: builder.mutation<IDeleteSuggestionResponse, IDeleteSuggestionRequest>({
        query: ({ token, id }) => {
          return {
            url: `/admin/suggestions/${id}`,
            method: 'DELETE',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
      updateSuggestion: builder.mutation<IUpdateSuggestionResponse, IUpdateSuggestionRequest>({
        query: ({ feedbackStatus, token, id }) => {
          return {
            url: `/admin/suggestions/${id}`,
            method: 'PATCH',
            body: { feedbackStatus },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
      createSuggestion: builder.mutation<ICreateSuggestionResponse, ICreateSuggestionRequest>({
        query: ({ token, body }) => {
          return {
            url: `/suggestions`,
            method: 'POST',
            body,
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
    };
  },
});
export const {
  useUpdateSuggestionMutation,
  useCreateSuggestionMutation,
  useLazyFetchSuggestionsQuery,
  useDeleteSuggestionMutation,
} = suggestionsApi;
export { suggestionsApi };
