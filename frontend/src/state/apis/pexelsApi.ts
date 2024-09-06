import { createApi } from '@reduxjs/toolkit/query/react';
import { IFetchPexelPhotosRequest, IFetchPexelPhotosResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const pexelApi = createApi({
  reducerPath: 'pexel',
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      fetchPexelPhotos: builder.query<IFetchPexelPhotosResponse, IFetchPexelPhotosRequest>({
        query: ({ token, query = 'sky' }) => {
          return {
            url: `/pexels?query=${query}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
    };
  },
});

export const { useLazyFetchPexelPhotosQuery, useFetchPexelPhotosQuery } = pexelApi;
export { pexelApi };
