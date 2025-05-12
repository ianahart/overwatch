import { screen, render, waitFor } from '@testing-library/react';
import { AllProviders } from '../../AllProviders';
import Form from '../../../src/components/ForgotPassword/Form';
import userEvent from '@testing-library/user-event';

describe('Form', () => {
  const renderComponent = () => {
    render(<Form />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      emailAddress: 'test@example.com',
      submitButton: screen.getByRole('button', { name: /continue/i }),
      emailInput: screen.getByPlaceholderText(/enter your email/i),
      heading: screen.getByRole('heading', { name: /reset your password/i }),
      link: screen.getByRole('link', { name: /return to sign in/i }),
    };
  };

  it('should render the form correctly', () => {
    const { submitButton, emailInput, heading, link } = renderComponent();

    expect(submitButton).toBeInTheDocument();
    expect(emailInput).toBeInTheDocument();
    expect(heading).toBeInTheDocument();
    expect(link).toBeInTheDocument();
  });

  it('should allow the user to type an email address', async () => {
    const { user, emailInput, emailAddress } = renderComponent();

    await user.type(emailInput, emailAddress);

    expect(emailInput).toHaveValue(emailAddress);
  });

  it('should call forgotPassword when form is submitted with valid email', async () => {
    const { user, emailInput, emailAddress, submitButton } = renderComponent();

    await user.type(emailInput, emailAddress);

    await user.click(submitButton);

    expect(await screen.findByText(/email successfully sent/i)).toBeInTheDocument();
    expect(screen.queryByPlaceholderText('Enter your email')).not.toBeInTheDocument();
  });

  it('should not call forgotPassword when form is submitted with invalid email', async () => {
    const { user, emailInput, submitButton } = renderComponent();

    await user.type(emailInput, ' ');

    await user.click(submitButton);

    expect(screen.queryByText(/email successfully sent/i)).not.toBeInTheDocument();
  });

  it('should not call forgotPassword when form is submitted with invalid email', async () => {
    const { user, emailInput, submitButton } = renderComponent();

    await user.type(emailInput, ' ');

    await user.click(submitButton);

    await waitFor(() => {
      expect(emailInput).toBeInTheDocument();
      expect(submitButton).toBeInTheDocument();
    });
  });
});
