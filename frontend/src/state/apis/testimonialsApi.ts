import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateTestimonialRequest,
  ICreateTestimonialResponse,
  IFetchTestimonialsRequest,
  IFetchTestimonialsResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const testimonialsApi = createApi({
  reducerPath: 'testimonials',
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      fetchTestimonials: builder.query<IFetchTestimonialsResponse, IFetchTestimonialsRequest>({
        query: ({ userId, token, page, pageSize, direction }) => {
          if (userId === 0 || userId === null) {
            return '';
          }
          return {
            url: `/testimonials?userId=${userId}&page=${page}&pageSize=${pageSize}&direction=${direction}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        //@ts-ignore
        providesTags: ['Testimonials'],
      }),

      createTestimonial: builder.mutation<ICreateTestimonialResponse, ICreateTestimonialRequest>({
        query: ({ userId, token, form }) => {
          return {
            url: `/testimonials`,
            method: 'POST',
            body: {
              userId,
              name: form.name.value,
              text: form.text.value,
            },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        // @ts-ignore
        invalidatesTags: ['Testimonials'],
      }),
    };
  },
});

export const { useCreateTestimonialMutation, useFetchTestimonialsQuery, useLazyFetchTestimonialsQuery } =
  testimonialsApi;
export { testimonialsApi };
