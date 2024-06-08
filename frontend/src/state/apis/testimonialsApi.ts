import { createApi } from '@reduxjs/toolkit/query/react';
import { ICreateTestimonialRequest, ICreateTestimonialResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const testimonialsApi = createApi({
  reducerPath: 'testimonials',
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
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

export const { useCreateTestimonialMutation } = testimonialsApi;
export { testimonialsApi };
