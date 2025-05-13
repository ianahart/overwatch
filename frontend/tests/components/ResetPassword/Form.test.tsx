import { screen, render, waitFor } from '@testing-library/react';
import Form from '../../../src/components/ResetPassword/Form';
import { AllProviders } from '../../AllProviders';
import userEvent, { UserEvent } from '@testing-library/user-event';
import { mockNavigate, mockUserSearchParams } from '../../setup';

describe('Form', () => {
  beforeEach(() => {
    mockNavigate.mockClear();
    vi.clearAllMocks();
  });

  afterEach(() => {
    mockNavigate.mockClear();
    vi.clearAllMocks();
  });

  const fillOutOTPInput = async (OTP: string, user: UserEvent, OTPInputs: HTMLElement[]) => {
    mockUserSearchParams({ token: OTP });

    const keyboard = OTP.split('').map((code, i) => user.type(OTPInputs[i], code));

    await Promise.all(keyboard);
  };

  const renderComponent = () => {
    render(<Form />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      submitButton: screen.getByRole('button', { name: /reset password/i }),
      passwordInput: screen.getByPlaceholderText(/create your new password/i),
      confirmPasswordInput: screen.getByPlaceholderText(/confirm your password/i),
      OTPInputs: screen.getAllByRole('textbox'),
    };
  };

  it('should render the form correctly', () => {
    const { passwordInput, confirmPasswordInput, OTPInputs, submitButton } = renderComponent();

    expect(passwordInput).toBeInTheDocument();
    expect(confirmPasswordInput).toBeInTheDocument();
    expect(submitButton).toBeInTheDocument();
    expect(Array.from(OTPInputs).length).toBe(5);
  });

  it('should display validation error for OTP input', async () => {
    const { user, OTPInputs, submitButton } = renderComponent();

    fillOutOTPInput('1234', user, OTPInputs);

    await user.click(submitButton);

    expect(screen.queryByText('Please provide the pass code that was in your email')).toBeInTheDocument();
  });

  it('should show validation errors for empty fields', async () => {
    const { user, OTPInputs, submitButton, confirmPasswordInput, passwordInput } = renderComponent();

    fillOutOTPInput('12345', user, OTPInputs);

    passwordInput.focus();
    await user.tab();
    confirmPasswordInput.focus();
    await user.tab();

    await user.click(submitButton);

    expect(await screen.findByText('password is required')).toBeInTheDocument();
    expect(await screen.findByText('confirmPassword is required')).toBeInTheDocument();
  });

  it('it successfully submits the form and navigates to the sign-in page', async () => {
    const { user, OTPInputs, submitButton, confirmPasswordInput, passwordInput } = renderComponent();

    mockUserSearchParams({ token: '12345' });

    await user.type(OTPInputs[0], '1');
    await user.type(OTPInputs[1], '2');
    await user.type(OTPInputs[2], '3');
    await user.type(OTPInputs[3], '4');
    await user.type(OTPInputs[4], '5');

    passwordInput.focus();
    await user.type(passwordInput, 'Test12345%');
    confirmPasswordInput.focus();
    await user.type(confirmPasswordInput, 'Test12345%');

    await user.click(submitButton);

    await waitFor(() => {
      expect(mockNavigate).toHaveBeenCalledWith('/signin');
    });
  });
});
