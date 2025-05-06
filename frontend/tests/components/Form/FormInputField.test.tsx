import { screen, render } from '@testing-library/react';
import FormInputField from '../../../src/components/Form/FormInputField';
import userEvent from '@testing-library/user-event';

describe('FormInputField', () => {
  const renderComponent = (propsOverride = {}) => {
    const handleUpdateField = vi.fn();
    const props = {
      handleUpdateField,
      name: 'firstName',
      id: 'firstName',
      errorField: 'First name',
      value: '',
      error: '',
      type: 'text',
      label: 'First Name',
      placeholder: 'Enter first name',
      ...propsOverride,
    };

    render(<FormInputField {...props} />);

    return {
      input: screen.getByPlaceholderText(props.placeholder) as HTMLInputElement,
      user: userEvent.setup(),
      props,
      handleUpdateField,
    };
  };

  it('should render input with correct label and placeholder', () => {
    const { input } = renderComponent();

    expect(screen.getByLabelText(/first name/i)).toBeInTheDocument();
    expect(input).toHaveAttribute('placeholder', 'Enter first name');
  });

  it('should call handleUpdateField on input change', async () => {
    const { user, input, handleUpdateField } = renderComponent();

    await user.click(input);
    await user.type(input, 'Jane');

    expect(handleUpdateField).toHaveBeenNthCalledWith(1, 'firstName', '', 'error');

    expect(handleUpdateField).toHaveBeenNthCalledWith(2, 'firstName', 'J', 'value');
    expect(handleUpdateField).toHaveBeenNthCalledWith(3, 'firstName', 'a', 'value');
    expect(handleUpdateField).toHaveBeenNthCalledWith(4, 'firstName', 'n', 'value');
    expect(handleUpdateField).toHaveBeenNthCalledWith(5, 'firstName', 'e', 'value');
  });

  it('should clear error on focus', async () => {
    const { user, input, handleUpdateField } = renderComponent();

    await user.click(input);

    expect(handleUpdateField).toHaveBeenNthCalledWith(1, 'firstName', '', 'error');
  });

  it('should validate length on blur and set error if invalid', async () => {
    const { user, input, handleUpdateField, props } = renderComponent();

    await user.type(input, 'J');
    await user.tab();

    expect(handleUpdateField).toHaveBeenCalledWith(
      props.name,
      `${props.errorField} must be between 1 and 200 characters`,
      'error'
    );
  });

  it('should not show error message if error is empty', () => {
    renderComponent();
    expect(screen.queryByText(/must be between/i)).not.toBeInTheDocument();
  });

  it('shows error message when error is passed in', () => {
    renderComponent({ error: 'First name is required' });
    expect(screen.getByText('First name is required')).toBeInTheDocument();
  });
});
