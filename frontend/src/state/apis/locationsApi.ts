import { createApi } from '@reduxjs/toolkit/query/react';
import { IFetchLocationsRequest, IFetchLocationsResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const locationsApi = createApi({
  reducerPath: 'locations',
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      fetchLocations: builder.query<IFetchLocationsResponse, IFetchLocationsRequest>({
        query: ({ text, token }) => {
          return {
            url: `/locations/autocomplete`,
            method: 'GET',
            params: {
              text,
            },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        // @ts-ignoree
        invalidatesTags: ['Location'],
      }),
    };
  },
});

export const { useLazyFetchLocationsQuery } = locationsApi;
export { locationsApi };
