import { http, HttpResponse } from 'msw';
import { baseURL } from '../../src/util';
import { paginate } from '../utils';
import { toPlainObject } from 'lodash';
import { createMinProfiles, createUserAndProfile } from '../mocks/dbActions';
import {
  ICreateAvatarResponse,
  IFetchProfilePackageResponse,
  IFetchProfileResponse,
  IFetchProfileVisibilityResponse,
  IPckgResponse,
  IRemoveAvatarResponse,
  IUpdateProfileResponse,
  IUpdateProfileVisibilityResponse,
} from '../../src/interfaces';
import { db } from '../mocks/db';

export const profileHandlers = [
  http.get(`${baseURL}/profiles/packages`, () => {
    const fullProfile = db.fullProfile.create();

    const data: IPckgResponse = {
      basic: { ...toPlainObject(fullProfile.pckg.basic) },
      standard: { ...toPlainObject(fullProfile.pckg.standard) },
      pro: { ...toPlainObject(fullProfile.pckg.pro) },
    };

    return HttpResponse.json<IFetchProfilePackageResponse>(
      {
        message: 'success',
        data,
      },
      { status: 200 }
    );
  }),

  http.patch(`${baseURL}/profiles/:profileId/avatar/remove`, () => {
    return HttpResponse.json<IRemoveAvatarResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),

  http.patch(`${baseURL}/profiles/:profileId/avatar/update`, () => {
    return HttpResponse.json<ICreateAvatarResponse>(
      {
        message: 'success',
        data: {
          avatarUrl: 'https://imgur.com/avatar',
        },
      },
      { status: 200 }
    );
  }),

  http.get(`${baseURL}/profiles/:profileId/visibility`, () => {
    const isVisible = true;

    return HttpResponse.json<IFetchProfileVisibilityResponse>(
      {
        message: 'success',
        data: isVisible,
      },
      { status: 200 }
    );
  }),

  http.patch(`${baseURL}/profiles/:profileId/visibility`, () => {
    const isVisible = true;

    return HttpResponse.json<IUpdateProfileVisibilityResponse>(
      {
        message: 'success',
        data: isVisible,
      },
      { status: 200 }
    );
  }),

  http.get(`${baseURL}/profiles/:profileId/populate`, () => {
    const data = createUserAndProfile({ userId: 1 });

    return HttpResponse.json<IFetchProfileResponse>(data, { status: 200 });
  }),

  http.get(`${baseURL}/profiles/:profileId`, () => {
    const data = createUserAndProfile({ userId: 1 });

    return HttpResponse.json<IFetchProfileResponse>(data, { status: 200 });
  }),

  http.get(`${baseURL}/profiles/all/:filter`, async ({ request }) => {
    const url = new URL(request.url);

    let pg = Number(url.searchParams.get('page')) ?? 1;
    const size = Number(url.searchParams.get('pageSize')) ?? 10;
    const dir = url.searchParams.get('direction') ?? 'next';
    const totalElements = 20;

    const data = createMinProfiles(10);

    const { page, totalPages, pageSize, direction, items } = paginate(pg, size, dir, data);

    return HttpResponse.json(
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
  http.patch(`${baseURL}/profiles/:profileId`, () => {
    return HttpResponse.json<IUpdateProfileResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),
];
