import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateTestimonialRequest,
  ICreateTestimonialResponse,
  IDeleteTestimonialRequest,
  IDeleteTestimonialResponse,
  IFetchTestimonialsRequest,
  IFetchTestimonialsResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const testimonialsApi = createApi({
  reducerPath: 'testimonials',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['Testimonial'],
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
        providesTags: (result, error, arg) =>
          result
            ? [...result.data.items.map(({ id }) => ({ type: 'Testimonial', id })), { type: 'Testimonial', id: 'LIST' }]
            : [{ type: 'Testimonial', id: 'LIST' }],
      }),

      deleteTestimonial: builder.mutation<IDeleteTestimonialResponse, IDeleteTestimonialRequest>({
        query: ({ id, token }) => ({
          url: `testimonials/${id}`,
          method: 'DELETE',
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }),
        //@ts-ignore
        invalidatesTags: (_, error, { id }) => [
          { type: 'Testimonial', id },
          { type: 'Testimonial', id: 'LIST' },
        ],
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
        invalidatesTags: [{ type: 'Testimonial', id: 'LIST' }],
      }),
    };
  },
});
export const {
  useDeleteTestimonialMutation,
  useCreateTestimonialMutation,
  useFetchTestimonialsQuery,
  useLazyFetchTestimonialsQuery,
} = testimonialsApi;
export { testimonialsApi };
