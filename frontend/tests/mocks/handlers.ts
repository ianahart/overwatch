import { http, HttpResponse } from 'msw';
import { db } from './db';
import { IGetTopicsWithTagsResponse, ISignInForm, ISignUpForm } from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { createSaveComments, createTopicWithTags, getTopicWithTags } from './dbActions';

export const handlers = [
  http.post(`${baseURL}/auth/login`, async ({ request }) => {
    const body = (await request.json()) as ISignInForm;
    if (body.email.value === 'test@example.com' && body.password.value === 'Test12345%') {
      return HttpResponse.json(db.user.create());
    }
    return HttpResponse.json({ message: 'Invalid credentials' }, { status: 401 });
  }),

  http.post(`${baseURL}/auth/register`, async ({ request }) => {
    const body = (await request.json()) as ISignUpForm;

    if (body.password.value === body.confirmPassword.value) {
      return HttpResponse.json({ message: 'User registered successfully' }, { status: 201 });
    }
    return HttpResponse.json({ message: 'Fields must not be empty' }, { status: 400 });
  }),

  http.get(`${baseURL}/topics`, async ({ request }) => {
    const url = new URL(request.url);
    const page = url.searchParams.get('page');
    const pageSize = url.searchParams.get('pageSize');
    const direction = url.searchParams.get('direction') ?? 'next';

    createTopicWithTags(20, 5);
    const items = getTopicWithTags(20);

    return HttpResponse.json<IGetTopicsWithTagsResponse>(
      {
        message: 'success',
        data: {
          items,
          page: Number(page),
          pageSize: Number(pageSize),
          totalPages: 2,
          direction,
          totalElements: items.length,
        },
      },
      { status: 200 }
    );
  }),
  http.get(`${baseURL}/save-comments`, async ({ request }) => {
    const url = new URL(request.url);
    const page = url.searchParams.get('page');
    const pageSize = url.searchParams.get('pageSize');
    const direction = url.searchParams.get('direction') ?? 'next';

    createSaveComments(10);

    const items = db.saveComment.findMany({});

    return HttpResponse.json(
      {
        message: 'success',
        data: {
          items,
          page: Number(page),
          pageSize: Number(pageSize),
          totalPages: 2,
          direction,
          totalElements: items.length,
        },
      },
      { status: 200 }
    );
  }),
];
