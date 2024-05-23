import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateLocationRequest,
  ICreateLocationResponse,
  IFetchLocationsRequest,
  IFetchLocationsResponse,
  IFetchSingleLocationRequest,
  IFetchSingleLocationResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const locationsApi = createApi({
  reducerPath: 'locations',
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      fetchSingleLocation: builder.query<IFetchSingleLocationResponse, IFetchSingleLocationRequest>({
        query: ({ userId, token }) => {
          if (userId === 0 || userId === null) {
            return '';
          }
          return {
            url: `/users/${userId}/locations`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        //@ts-ignore
        providesTags: ['Location'],
      }),

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
      }),
      createLocation: builder.mutation<ICreateLocationResponse, ICreateLocationRequest>({
        query: ({ form, token, userId }) => {
          return {
            url: `/users/${userId}/locations`,
            method: 'POST',
            body: {
              address: form.address.value,
              addressTwo: form.addressTwo.value,
              city: form.city.value,
              country: form.country.value,
              phoneNumber: form.phoneNumber.value,
              state: form.state.value,
              zipCode: form.zipCode.value,
            },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        // @ts-ignore
        invalidatesTags: ['Location'],
      }),
    };
  },
});

export const { useLazyFetchLocationsQuery, useCreateLocationMutation, useFetchSingleLocationQuery } = locationsApi;
export { locationsApi };
