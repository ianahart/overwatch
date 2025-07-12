import { http, HttpResponse } from 'msw';
import { toPlainObject } from 'lodash';

import {
  ICreateAppTestimonialResponse,
  IDeleteAppTestimonialResponse,
  IGetAppTestimonialResponse,
  IMinAppTestimonial,
  IUpdateAppTestimonialResponse,
} from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { db } from '../mocks/db';

export const appTestimonialsHandlers = [
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

  http.delete(`${baseURL}/app-testimonials/:id`, () => {
    return HttpResponse.json<IDeleteAppTestimonialResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),
];
