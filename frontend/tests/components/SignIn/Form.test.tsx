import { screen, render, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { HttpResponse, delay, http } from 'msw';

import { baseURL } from '../../../src/util';
import { server } from '../../mocks/server';
import Form from '../../../src/components/SignIn/Form';
import { AllProviders } from '../../AllProviders';
import { db } from '../../mocks/db';
import { mockNavigate } from '../../setup';

describe('Form', () => {
  const renderComponent = () => {
    render(<Form />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      navigate: mockNavigate,
      emailInput: screen.getByLabelText(/email/i),
      passwordInput: screen.getByLabelText(/password/i),
      signInButton: screen.getByRole('button', { name: /sign in/i }),
    };
  };
  it('should render the form fields correctly', () => {
    const { emailInput, passwordInput, signInButton } = renderComponent();

    expect(emailInput).toBeInTheDocument();
    expect(passwordInput).toBeInTheDocument();
    expect(signInButton).toBeInTheDocument();
  });

  it('should show error message when form is submitted with empty fields', async () => {
    const { user, signInButton } = renderComponent();

    await user.click(signInButton);

    const errorMessages = await screen.findAllByText(/field must not be empty/i);

    expect(errorMessages.length).toBeGreaterThan(0);
  });

  it('should show error message when login fails', async () => {
    server.use(
      http.post(`${baseURL}/auth/login`, async () => {
        return HttpResponse.json({ message: 'Invalid credentials' }, { status: 401 });
      })
    );
    const { user, signInButton, emailInput, passwordInput } = renderComponent();

    await user.type(emailInput, 'test@example.com');
    await user.type(passwordInput, 'Test1234%');

    await user.click(signInButton);

    expect(await screen.findByText(/invalid credentials/i)).toBeInTheDocument();
  });

  it('should navigate to the correct page when login is successful', async () => {
    const userEntity = db.user.create();
    server.use(
      http.post(`${baseURL}/auth/login`, async () => {
        return HttpResponse.json({ user: userEntity }, { status: 200 });
      })
    );

    const { emailInput, navigate, signInButton, passwordInput, user } = renderComponent();

    await user.type(emailInput, 'test@example.com');
    await user.type(passwordInput, 'Test12345%');

    await userEvent.click(signInButton);

    await waitFor(() => {
      expect(navigate).toHaveBeenCalledWith(`/dashboard/${userEntity.slug}`);
    });
  });

  it('should show a spinner when form is submitting', async () => {
    const userEntity = db.user.create();
    server.use(
      http.post(`${baseURL}/auth/login`, async () => {
        await delay(100);
        return HttpResponse.json({ user: userEntity }, { status: 200 });
      })
    );

    const { emailInput, signInButton, passwordInput, user } = renderComponent();

    await user.type(emailInput, 'test@example.com');
    await user.type(passwordInput, 'Test12345%');

    await userEvent.click(signInButton);

    expect(screen.getByText(/signing in.../i)).toBeInTheDocument();
  });
});
