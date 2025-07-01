import { http, HttpResponse } from 'msw';

import { baseURL } from '../../src/util';
import {
  ICreateLocationResponse,
  IFetchLocationsResponse,
  IFetchSingleLocationResponse,
  ILocation,
} from '../../src/interfaces';

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
    const location: ILocation = {
      address: '123 Main St',
      addressTwo: '',
      phoneNumber: '1111111111',
      city: 'Springfield',
      country: 'United States',
      state: 'IL',
      zipCode: '62704',
    };

    return HttpResponse.json<IFetchSingleLocationResponse>(
      {
        message: 'success',
        data: location,
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
