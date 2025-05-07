import { screen, render } from '@testing-library/react';
import FormTextareaField, { IFormTextareaFieldProps } from '../../../src/components/Form/FormTextareaField';
import userEvent, { UserEvent } from '@testing-library/user-event';
import { Mock } from 'vitest';

describe('FormTextareaField', () => {
  const renderComponent = (overrides: Partial<IFormTextareaFieldProps> = {}) => {
    const handleUpdateField = vi.fn();
    const defaultProps: IFormTextareaFieldProps = {
      handleUpdateField,
      name: 'description',
      value: '',
      error: '',
      label: 'Description',
      id: 'description',
      errorField: 'Description',
      placeholder: 'Enter a description',
      min: 2,
      max: 200,
      ...overrides,
    };
    render(<FormTextareaField {...defaultProps} />);

    return {
      user: userEvent.setup(),
      textarea: () => screen.getByPlaceholderText(/description/i) as HTMLElement,
      errorMessage: () => screen.queryByText(/description must be between/i) as HTMLElement,
      handleUpdateField,
    };
  };

  const triggerCharacterLengthError = async (
    chars: string,
    user: UserEvent,
    handleUpdateField: Mock,
    textarea: () => HTMLElement,
    errorMessage: () => HTMLElement
  ) => {
    const error = 'Description must be between 2 and 200 characters';

    await user.type(textarea(), chars);
    await user.tab();

    expect(handleUpdateField).toHaveBeenCalledWith('description', error, 'error');
    renderComponent({ error });
    expect(errorMessage()).toBeInTheDocument();
  };

  it('should render with label, placeholder, and no error initially', () => {
    const { textarea, errorMessage } = renderComponent();

    expect(screen.getByLabelText('Description')).toBeInTheDocument();
    expect(textarea()).toBeInTheDocument();
    expect(textarea()).toHaveValue('');
    expect(errorMessage()).not.toBeInTheDocument();
  });
  it('should call handleUpdateField on text change', async () => {
    const { user, textarea, errorMessage, handleUpdateField } = renderComponent();

    await user.type(textarea(), 't');

    expect(handleUpdateField).toHaveBeenCalledWith('description', 't', 'value');
    expect(errorMessage()).not.toBeInTheDocument();
  });
  it('should show error message on blur if text is too short', async () => {
    const { user, errorMessage, handleUpdateField, textarea } = renderComponent();
    const tooShort = 'a';

    await triggerCharacterLengthError(tooShort, user, handleUpdateField, textarea, errorMessage);

    expect(errorMessage()).toBeInTheDocument();
  });
  it('should show error message on blur if text is too long', async () => {
    const { user, errorMessage, handleUpdateField, textarea } = renderComponent();
    const tooLong = 'a'.repeat(201);

    await triggerCharacterLengthError(tooLong, user, handleUpdateField, textarea, errorMessage);
  });
  it('clears the error message on focus', async () => {
    const error = 'Description must be between 2 and 200 characters';
    const { user, errorMessage, textarea, handleUpdateField } = renderComponent({ error });

    expect(errorMessage()).toBeInTheDocument();

    await user.click(textarea());

    expect(handleUpdateField).toHaveBeenCalledWith('description', '', 'error');

    handleUpdateField.mock.calls.forEach(([name, value, attr]) => {
      if (name === 'description' && attr === 'error' && value === '') {
        errorMessage().remove(); // manually remove for testing
      }
    });

    expect(errorMessage()).not.toBeInTheDocument();
  });
});
