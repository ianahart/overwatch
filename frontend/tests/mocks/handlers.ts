import { http, HttpResponse } from 'msw';
import { db } from './db';
import { ISignInForm, ISignUpForm } from '../../src/interfaces';
import { baseURL } from '../../src/util';

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
];
