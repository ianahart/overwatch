import { http, HttpResponse } from 'msw';
import { db } from '../mocks/db';
import {
  IForgotPasswordForm,
  IResetPasswordForm,
  IResetPasswordResponse,
  ISignInForm,
  ISignUpForm,
  IUser,
  IVerifyOTPRequest,
  IVerifyOTPResponse,
} from '../../src/interfaces';
import { baseURL } from '../../src/util';
import { toPlainObject } from 'lodash';

export const authHandlers = [
  http.post(`${baseURL}/auth/verify-otp`, async ({ request }) => {
    const body = (await request.json()) as IVerifyOTPRequest;

    if (!body.otpCode || !body.userId) {
      return HttpResponse.json(
        {
          message: 'Missing otpCode or userId',
        },
        { status: 400 }
      );
    }
    const userEnity = db.user.create();
    const tokenEntity = db.token.create();

    const user: IUser = { ...toPlainObject(userEnity), slug: 'slug', id: 1 };

    return HttpResponse.json<IVerifyOTPResponse>(
      {
        token: tokenEntity.token,
        refreshToken: tokenEntity.refreshToken,
        user: toPlainObject(user),
        userId: user.id,
      },
      { status: 200 }
    );
  }),

  http.get(`${baseURL}/auth/generate-otp`, async () => {
    return HttpResponse.json(
      {
        data: 123456,
      },
      { status: 200 }
    );
  }),

  http.post(`${baseURL}/auth/reset-password`, async ({ request }) => {
    const body = (await request.json()) as IResetPasswordForm;

    if (body.confirmPassword !== body.password) {
      return HttpResponse.json(
        {
          message: 'Passwords do not match',
        },
        { status: 400 }
      );
    }
    return HttpResponse.json<IResetPasswordResponse>(
      {
        message: 'success',
      },
      { status: 200 }
    );
  }),

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
