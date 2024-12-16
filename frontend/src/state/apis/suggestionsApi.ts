import { createApi } from '@reduxjs/toolkit/query/react';
import { ICreateSuggestionRequest, ICreateSuggestionResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const suggestionsApi = createApi({
  reducerPath: 'suggestions',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['Suggestion'],
  endpoints(builder) {
    return {
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
        invalidatesTags: [{ type: 'Suggestion', id: 'LIST' }],
      }),
    };
  },
});
export const { useCreateSuggestionMutation } = suggestionsApi;
export { suggestionsApi };
