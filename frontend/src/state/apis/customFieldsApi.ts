import { createApi } from '@reduxjs/toolkit/query/react';
import { ICreateCustomFieldRequest, ICreateCustomFieldResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const customFieldsApi = createApi({
  reducerPath: 'customFields',
  tagTypes: ['CustomField', 'TodoCard'],
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
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
    };
  },
});

export const { useCreateCustomFieldMutation } = customFieldsApi;
export { customFieldsApi };
