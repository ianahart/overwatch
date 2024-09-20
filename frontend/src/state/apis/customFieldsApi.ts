import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateCustomFieldRequest,
  ICreateCustomFieldResponse,
  IFetchCustomFieldsRequest,
  IFetchCustomFieldsResponse,
  IDeleteCustomFieldRequest,
  IDeleteCustomFieldResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const customFieldsApi = createApi({
  reducerPath: 'customFields',
  tagTypes: ['CustomField', 'TodoCard'],
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      fetchCustomFields: builder.query<IFetchCustomFieldsResponse, IFetchCustomFieldsRequest>({
        query: ({ token, todoCardId }) => {
          if (todoCardId === 0 || todoCardId === null) {
            return '';
          }
          return {
            url: `/todo-cards/${todoCardId}/custom-fields`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        //@ts-ignore
        providesTags: (result, error, arg) =>
          result
            ? [...result.data.map(({ id }) => ({ type: 'CustomField', id })), { type: 'CustomField', id: 'LIST' }]
            : [{ type: 'CustomField', id: 'LIST' }],
      }),

      createCustomField: builder.mutation<ICreateCustomFieldResponse, ICreateCustomFieldRequest>({
        query: ({ todoCardId, userId, fieldName, selectedValue, dropDownOptions, fieldType, token }) => {
          return {
            url: `/custom-fields`,
            method: 'POST',
            headers: {
              Authorization: `Bearer ${token}`,
            },
            body: {
              todoCardId,
              userId,
              fieldName,
              selectedValue,
              dropDownOptions,
              fieldType,
            },
          };
        },
        invalidatesTags: (result, error, { todoCardId }) => {
          console.log(result, error, todoCardId);
          return [
            { type: 'CustomField', id: 'LIST' },
            { type: 'TodoCard', id: todoCardId },
          ];
        },
      }),
      deleteCustomField: builder.mutation<IDeleteCustomFieldResponse, IDeleteCustomFieldRequest>({
        query: ({ id, token }) => ({
          url: `custom-fields/${id}`,
          method: 'DELETE',
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }),
        //@ts-ignore
        invalidatesTags: (_, error, { id }) => [
          { type: 'CustomField', id: id },
          { type: 'CustomField', id: 'LIST' },
        ],
      }),
    };
  },
});

export const { useCreateCustomFieldMutation, useFetchCustomFieldsQuery, useDeleteCustomFieldMutation } =
  customFieldsApi;
export { customFieldsApi };
