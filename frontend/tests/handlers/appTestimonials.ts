import { http, HttpResponse } from 'msw';
import { toPlainObject } from 'lodash';

import {
  ICreateAppTestimonialResponse,
  IDeleteAppTestimonialResponse,
  IGetAllAdminAppTestimonialsResponse,
  IGetAppTestimonialResponse,
  IMinAppTestimonial,
  IUpdateAdminAppTestimonialResponse,
  IUpdateAppTestimonialResponse,
} from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { db } from '../mocks/db';
import { createAppTestimonials } from '../mocks/dbActions';
import { paginate } from '../utils';

export const appTestimonialsHandlers = [
  http.get(`${baseURL}/admin/app-testimonials`, async ({ request }) => {
    const url = new URL(request.url);

    let pg = Number(url.searchParams.get('page')) ?? 1;
    const size = 2;
    const dir = url.searchParams.get('direction') ?? 'next';
    const totalElements = 4;

    const data = createAppTestimonials(size);

    const { page, totalPages, pageSize, direction, items } = paginate(pg, size, dir, data);

    return HttpResponse.json<IGetAllAdminAppTestimonialsResponse>(
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

  http.get(`${baseURL}/app-testimonials/single`, () => {
    const data: IMinAppTestimonial = { ...toPlainObject(db.minAppTestimonial.create()) };

    return HttpResponse.json<IGetAppTestimonialResponse>(
      {
        message: 'success',
        data,
      },
      { status: 200 }
    );
  }),

  http.get(`${baseURL}/app-testimonials`, () => {
    return HttpResponse.json<ICreateAppTestimonialResponse>(
      {
        message: 'success',
      },
      { status: 201 }
    );
  }),

  http.patch(`${baseURL}/app-testimonials/:id`, () => {
    return HttpResponse.json<IUpdateAppTestimonialResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),

  http.patch(`${baseURL}/admin/app-testimonials/:id`, () => {
    return HttpResponse.json<IUpdateAdminAppTestimonialResponse>(
      {
        message: 'success',
        data: true,
      },
      { status: 200 }
    );
  }),

  http.delete(`${baseURL}/app-testimonials/:id`, () => {
    return HttpResponse.json<IDeleteAppTestimonialResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),
];
