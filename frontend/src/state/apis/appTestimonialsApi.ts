import { createApi } from '@reduxjs/toolkit/query/react';
import { ICreateAppTestimonialRequest, ICreateAppTestimonialResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const appTestimonialsApi = createApi({
  reducerPath: 'appTestimonials',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['AppTestimonial'],
  endpoints(builder) {
    return {
      createAppTestimonial: builder.mutation<ICreateAppTestimonialResponse, ICreateAppTestimonialRequest>({
        query: ({ developerType, content, token, userId }) => {
          return {
            url: `/app-testimonials`,
            method: 'POST',
            body: { developerType, content, userId },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        invalidatesTags: [{ type: 'AppTestimonial', id: 'LIST' }],
      }),
    };
  },
});
export const { useCreateAppTestimonialMutation } = appTestimonialsApi;
export { appTestimonialsApi };
