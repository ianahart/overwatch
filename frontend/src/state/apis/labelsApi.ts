import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateLabelRequest,
  ICreateLabelResponse,
  IFetchLabelRequest,
  IFetchLabelResponse,
  IDeleteLabelRequest,
  IDeleteLabelResponse,
  IUpdateLabelResponse,
  IUpdateLabelRequest,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const labelsApi = createApi({
  reducerPath: 'labels',
  tagTypes: ['Label', 'ActiveLabel'],
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      fetchLabels: builder.query<IFetchLabelResponse, IFetchLabelRequest>({
        query: ({ token, workSpaceId }) => {
          if (workSpaceId === 0 || workSpaceId === null) {
            return '';
          }
          return {
            url: `/labels?workSpaceId=${workSpaceId}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        //@ts-ignore
        providesTags: (result, error, arg) =>
          result
            ? [...result.data.map(({ id }) => ({ type: 'Label', id })), { type: 'Label', id: 'LIST' }]
            : [{ type: 'Label', id: 'LIST' }],
      }),

      createLabel: builder.mutation<ICreateLabelResponse, ICreateLabelRequest>({
        query: ({ userId, token, title, color, workSpaceId }) => {
          return {
            url: `/labels`,
            method: 'POST',
            headers: {
              Authorization: `Bearer ${token}`,
            },
            body: {
              userId,
              title,
              color,
              workSpaceId,
            },
          };
        },
        invalidatesTags: (result, error, { workSpaceId }) => {
          console.log(result, error, workSpaceId);
          return [{ type: 'Label', id: 'LIST' }];
        },
      }),
      updateLabel: builder.mutation<IUpdateLabelResponse, IUpdateLabelRequest>({
        query: ({ token, label }) => ({
          url: `labels/${label.id}`,
          method: 'PATCH',
          headers: {
            Authorization: `Bearer ${token}`,
          },
          body: { isChecked: label.isChecked },
        }),
        //@ts-ignore
        invalidatesTags: (_, error, { id }) => [
          { type: 'Label', id: id },
          { type: 'Label', id: 'LIST' },
        ],
      }),

      deleteLabel: builder.mutation<IDeleteLabelResponse, IDeleteLabelRequest>({
        query: ({ id, token }) => ({
          url: `labels/${id}`,
          method: 'DELETE',
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }),
        //@ts-ignore
        invalidatesTags: (_, error, { id }) => [
          { type: 'Label', id: id },
          { type: 'Label', id: 'LIST' },
          { type: 'ActiveLabel', id: 'LIST' },
        ],
      }),
    };
  },
});

export const { useUpdateLabelMutation, useDeleteLabelMutation, useCreateLabelMutation, useFetchLabelsQuery } =
  labelsApi;
export { labelsApi };
