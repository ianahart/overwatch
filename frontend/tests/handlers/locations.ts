import { http, HttpResponse } from 'msw';
import { toPlainObject } from 'lodash';

import { baseURL } from '../../src/util';
import { ICreateLocationResponse, IFetchLocationsResponse, IFetchSingleLocationResponse } from '../../src/interfaces';
import { db } from '../mocks/db';

export const locationHandlers = [
  http.get(`${baseURL}/locations/autocomplete`, () => {
    const addressResults = [
      {
        place_id: 'abc123',
        formatted: '123 Main St, Springfield, IL',
        city: 'Springfield',
        country: 'United States',
        county: '',
        housenumber: '123',
        state: 'IL',
        street: 'Main St',
        zipCode: '62704',
      },
    ];
    return HttpResponse.json<IFetchLocationsResponse>(
      {
        message: 'success',
        data: JSON.stringify([{ properties: { ...addressResults[0] } }]),
      },
      { status: 200 }
    );
  }),

  http.get(`${baseURL}/users/:userId/locations`, () => {
    const location = toPlainObject(db.location.create());

    return HttpResponse.json<IFetchSingleLocationResponse>(
      {
        message: 'success',
        ...location,
      },
      { status: 200 }
    );
  }),

  http.post(`${baseURL}/users/:userId/locations`, () => {
    return HttpResponse.json<ICreateLocationResponse>(
      {
        message: 'success',
      },
      { status: 201 }
    );
  }),
];
