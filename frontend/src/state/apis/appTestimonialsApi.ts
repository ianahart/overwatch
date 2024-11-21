import { createApi } from '@reduxjs/toolkit/query/react';
import {
  ICreateAppTestimonialRequest,
  ICreateAppTestimonialResponse,
  IDeleteAppTestimonialRequest,
  IDeleteAppTestimonialResponse,
  IGetAppTestimonialRequest,
  IGetAppTestimonialResponse,
  IUpdateAppTestimonialRequest,
  IUpdateAppTestimonialResponse,
} from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const appTestimonialsApi = createApi({
  reducerPath: 'appTestimonials',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['AppTestimonial'],
  endpoints(builder) {
    return {
      fetchAppTestimonial: builder.query<IGetAppTestimonialResponse, IGetAppTestimonialRequest>({
        query: ({ token }) => {
          if (!token) {
            return '';
          }
          return {
            url: '/app-testimonials/single',
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
      updateAppTestimonial: builder.mutation<IUpdateAppTestimonialResponse, IUpdateAppTestimonialRequest>({
        query: ({ developerType, content, token, userId, id }) => {
          return {
            url: `/app-testimonials/${id}`,
            method: 'PATCH',
            body: { developerType, content, userId },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        invalidatesTags: [{ type: 'AppTestimonial', id: 'LIST' }],
      }),
      deleteAppTestimonial: builder.mutation<IDeleteAppTestimonialResponse, IDeleteAppTestimonialRequest>({
        query: ({ token, id }) => {
          return {
            url: `/app-testimonials/${id}`,
            method: 'DELETE',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
        invalidatesTags: [{ type: 'AppTestimonial', id: 'LIST' }],
      }),

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
export const {
  useDeleteAppTestimonialMutation,
  useUpdateAppTestimonialMutation,
  useCreateAppTestimonialMutation,
  useFetchAppTestimonialQuery,
} = appTestimonialsApi;
export { appTestimonialsApi };
