import { http, HttpResponse } from 'msw';
import { db } from '../mocks/db';
import { IForgotPasswordForm, ISignInForm, ISignUpForm } from '../../src/interfaces';
import { baseURL } from '../../src/util';

export const authHandlers = [
  http.post(`${baseURL}/auth/forgot-password`, async ({ request }) => {
    const body = (await request.json()) as IForgotPasswordForm;

    if (!body.email) {
      return HttpResponse.json({ message: 'A user with that email does not exist' }, { status: 404 });
    }

    return HttpResponse.json(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),

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
