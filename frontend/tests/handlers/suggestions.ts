import { http, HttpResponse } from 'msw';

import { ICreateSuggestionResponse, IGetAllSuggestionsResponse } from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { createSuggestions } from '../mocks/dbActions';
import { paginate } from '../utils';

export const suggestionsHandlers = [
  http.patch(`${baseURL}/admin/suggestions/:id`, () => {
    return HttpResponse.json(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),

  http.delete(`${baseURL}/admin/suggestions/:id`, () => {
    return HttpResponse.json(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),

  http.get(`${baseURL}/admin/suggestions`, async ({ request }) => {
    const url = new URL(request.url);

    let pg = Number(url.searchParams.get('page')) ?? 1;
    const size = 2;
    const dir = url.searchParams.get('direction') ?? 'next';
    const totalElements = 6;

    const data = createSuggestions(totalElements);

    const { page, totalPages, pageSize, direction, items } = paginate(pg, size, dir, data);

    return HttpResponse.json<IGetAllSuggestionsResponse>(
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

  http.post(`${baseURL}/suggestions`, () => {
    return HttpResponse.json<ICreateSuggestionResponse>(
      {
        message: 'success',
      },
      { status: 201 }
    );
  }),
];
