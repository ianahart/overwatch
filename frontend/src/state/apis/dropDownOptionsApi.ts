import { createApi } from '@reduxjs/toolkit/query/react';
import { IDeleteDropDownOptionRequest, IDeleteDropDownOptionResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const dropDownOptionsApi = createApi({
  reducerPath: 'dropDownOptions',
  tagTypes: ['DropDownOption'],
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      deleteDropDownOption: builder.mutation<IDeleteDropDownOptionResponse, IDeleteDropDownOptionRequest>({
        query: ({ id, token }) => ({
          url: `dropdown-options/${id}`,
          method: 'DELETE',
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }),
        //@ts-ignore
        invalidatesTags: (_, error, { id }) => [
          { type: 'DropDownOption', id: id },
          { type: 'DropDownOption', id: 'LIST' },
        ],
      }),
    };
  },
});

export const { useDeleteDropDownOptionMutation } = dropDownOptionsApi;
export { dropDownOptionsApi };
