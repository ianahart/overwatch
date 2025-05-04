import { screen, render, waitFor } from '@testing-library/react';
import userEvent, { UserEvent } from '@testing-library/user-event';

import Form from '../../../src/components/SignUp/Form';
import { AllProviders } from '../../AllProviders';
import { mockNavigate } from '../../setup';
import { server } from '../../mocks/server';
import { HttpResponse, http } from 'msw';
import { baseURL } from '../../../src/util';
import { Mock } from 'vitest';

export interface ITestForm {
  user: UserEvent;
  navigate: Mock<any>;
  firstNameInput: HTMLElement;
  lastNameInput: HTMLElement;
  emailInput: HTMLElement;
  passwordInput: HTMLElement;
  confirmPasswordInput: HTMLElement;
  signUpButton: HTMLElement;
}

describe('Form', () => {
  const fillForm = async (form: ITestForm) => {
    await form.user.type(form.firstNameInput, 'Jane');
    await form.user.type(form.lastNameInput, 'Doe');
    await form.user.type(form.emailInput, 'jane@example.com');
    await form.user.type(form.passwordInput, 'Test12345%');
    await form.user.type(form.confirmPasswordInput, 'Test12345%');
  };

  const renderComponent = () => {
    render(<Form />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      navigate: mockNavigate,
      firstNameInput: screen.getByLabelText(/first name/i),
      lastNameInput: screen.getByLabelText(/last name/i),
      emailInput: screen.getByLabelText(/email/i),
      passwordInput: screen.getByPlaceholderText(/create password/i),
      confirmPasswordInput: screen.getByPlaceholderText(/confirm your password/i),
      signUpButton: screen.getByRole('button', { name: /sign up/i }),
    };
  };

  it('should submit form and navigation to /signin on success', async () => {
    const form = renderComponent();

    await fillForm(form);

    await form.user.click(screen.getByText(/I am looking for someone to review my code/i));
    await form.user.click(form.signUpButton);

    await waitFor(() => {
      expect(form.navigate).toHaveBeenCalledWith(`/signin`);
    });
  });
  it('should display server side validation error', async () => {
    server.use(
      http.post(`${baseURL}/auth/register`, async () =>
        HttpResponse.json({ email: 'A user with this email already exists' }, { status: 400 })
      )
    );
    const form = renderComponent();

    await fillForm(form);

    await form.user.click(screen.getByText(/I am looking for someone to review my code/i));
    await form.user.click(form.signUpButton);

    await waitFor(() => {
      expect(screen.getByText(/already exists/i)).toBeInTheDocument();
    });
  });
});
