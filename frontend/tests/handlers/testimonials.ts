import { http, HttpResponse } from 'msw';

import { IFetchTopTestimonialsResponse } from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { createTestimonials } from '../mocks/dbActions';

export const testimonialsHandlers = [
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
