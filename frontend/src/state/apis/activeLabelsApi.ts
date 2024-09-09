import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateActiveLabelResponse,
  IDeleteActiveLabelRequest,
  IDeleteActiveLabelResponse,
  ICreateActiveLabelRequest,
  IFetchActiveLabelsResponse,
  IFetchActiveLabelsRequest,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const activeLabelsApi = createApi({
  reducerPath: 'activeLabels',
  tagTypes: ['ActiveLabel'],
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      createActiveLabel: builder.mutation<ICreateActiveLabelResponse, ICreateActiveLabelRequest>({
        query: ({ cardId, labelId, token }) => {
          return {
            url: `/active-labels`,
            method: 'POST',
            headers: {
              Authorization: `Bearer ${token}`,
            },
            body: {
              todoCardId: cardId,
              labelId,
            },
          };
        },
        invalidatesTags: (result, error, { cardId }) => {
          console.log(result, error, cardId);
          return [{ type: 'ActiveLabel', id: 'LIST' }];
        },
      }),
      fetchActiveLabels: builder.query<IFetchActiveLabelsResponse, IFetchActiveLabelsRequest>({
        query: ({ token, todoCardId }) => {
          if (todoCardId === 0 || todoCardId === null) {
            return '';
          }
          return {
            url: `/active-labels?todoCardId=${todoCardId}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        //@ts-ignore
        providesTags: (result, error, arg) =>
          result
            ? [...result.data.map(({ id }) => ({ type: 'ActiveLabel', id })), { type: 'ActiveLabel', id: 'LIST' }]
            : [{ type: 'ActiveLabel', id: 'LIST' }],
      }),
      deleteActiveLabel: builder.mutation<IDeleteActiveLabelResponse, IDeleteActiveLabelRequest>({
        query: ({ id, token, labelId }) => ({
          url: `active-labels/${id}?labelId=${labelId}`,
          method: 'DELETE',
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }),
        //@ts-ignore
        invalidatesTags: (_, error, { id }) => [
          { type: 'ActiveLabel', id: id },
          { type: 'ActiveLabel', id: 'LIST' },
        ],
      }),
    };
  },
});

export const { useFetchActiveLabelsQuery, useDeleteActiveLabelMutation, useCreateActiveLabelMutation } =
  activeLabelsApi;
export { activeLabelsApi };
