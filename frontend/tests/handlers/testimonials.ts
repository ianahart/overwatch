import { http, HttpResponse } from 'msw';

import {
  IDeleteTestimonialResponse,
  IFetchTestimonialsResponse,
  IFetchTopTestimonialsResponse,
} from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { createTestimonials } from '../mocks/dbActions';
import { paginate } from '../utils';

export const testimonialsHandlers = [
  http.get(`${baseURL}/testimonials`, async ({ request }) => {
    const url = new URL(request.url);

    let pg = Number(url.searchParams.get('page')) ?? 1;
    const size = 2;
    const dir = url.searchParams.get('direction') ?? 'next';
    const totalElements = 6;
    const totalPages = Math.ceil(totalElements / size);

    const data = createTestimonials(totalElements);

    const { page, pageSize, direction, items } = paginate(pg, size, dir, data);

    return HttpResponse.json<IFetchTestimonialsResponse>(
      {
        message: 'success',
        data: {
          items,
          page,
          pageSize,
          totalPages,
          direction,
          totalElements,
        },
      },
      { status: 200 }
    );
  }),

  http.delete(`${baseURL}/testimonials/:id`, () => {
    return HttpResponse.json<IDeleteTestimonialResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),

  http.get(`${baseURL}/testimonials/latest`, async () => {
    const data = createTestimonials(3);
    return HttpResponse.json<IFetchTopTestimonialsResponse>(
      {
        message: 'success',
        data,
      },
      { status: 200 }
    );
  }),
];
