import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { AiOutlineLock } from 'react-icons/ai';
import FormInputPasswordField, {
  IFormInputPasswordFieldProps,
} from '../../../src/components/Form/FormInputPasswordField';

describe('FormInputPasswordField', () => {
  const mockHandleUpdateField = vi.fn();

  const defaultProps: IFormInputPasswordFieldProps = {
    name: 'password',
    id: 'password',
    label: 'Password',
    placeholder: 'Enter password',
    type: 'password',
    value: '',
    error: '',
    icon: <AiOutlineLock />,
    errorField: 'Password',
    visibility: true,
    handleUpdateField: mockHandleUpdateField,
  };

  const renderComponent = (props = {}) => {
    mockHandleUpdateField.mockClear();
    render(<FormInputPasswordField {...defaultProps} {...props} />);

    return {
      user: userEvent.setup(),
      passwordInput: screen.getByLabelText('Password') as HTMLInputElement,
    };
  };

  it('should render input with correct label and placeholder', () => {
    const { passwordInput } = renderComponent();

    expect(passwordInput).toBeInTheDocument();
    expect(screen.getByPlaceholderText('Enter password')).toBeInTheDocument();
  });

  it('should call mockHandleUpdateField on input change', async () => {
    const { user, passwordInput } = renderComponent();
    renderComponent({ value: '' });

    await user.click(passwordInput);
    await user.type(passwordInput, 'Test1');

    expect(mockHandleUpdateField).toHaveBeenNthCalledWith(1, 'password', '', 'error');

    expect(mockHandleUpdateField).toHaveBeenNthCalledWith(2, 'password', 'T', 'value');
    expect(mockHandleUpdateField).toHaveBeenNthCalledWith(3, 'password', 'e', 'value');
    expect(mockHandleUpdateField).toHaveBeenNthCalledWith(4, 'password', 's', 'value');
    expect(mockHandleUpdateField).toHaveBeenNthCalledWith(5, 'password', 't', 'value');
    expect(mockHandleUpdateField).toHaveBeenNthCalledWith(6, 'password', '1', 'value');
  });

  it('should show error on blur if password is too short', async () => {
    const { passwordInput, user } = renderComponent({ value: '', min: 1 });

    await user.click(passwordInput);
    await user.tab();
    expect(mockHandleUpdateField).toHaveBeenCalledWith(
      'password',
      'Password must be between 1 and 200 characters',
      'error'
    );
  });

  it('should clear error on focus', async () => {
    const { user, passwordInput } = renderComponent();

    await user.click(passwordInput);

    expect(mockHandleUpdateField).toHaveBeenNthCalledWith(1, 'password', '', 'error');
  });

  it('should toggle visibility when eye icon is clicked', async () => {
    const { user } = renderComponent({ type: 'password', visibility: true });

    const toggle = screen.getByLabelText(/eye outline icon/i);
    await user.click(toggle);

    expect(mockHandleUpdateField).toHaveBeenCalledWith('password', 'text', 'type');
    expect(mockHandleUpdateField).toHaveBeenCalledWith('confirmPassword', 'text', 'type');
  });

  it('should show error message when error exists', () => {
    renderComponent({ error: 'This is an error' });
    expect(screen.getByText('This is an error')).toBeInTheDocument();
  });

  it('should render PasswordStrengthMeter if visibility is true and password is not empty', () => {
    renderComponent({ value: 'abc123', visibility: true });
    expect(screen.getByText(/weak/i)).toBeInTheDocument();
  });
});
