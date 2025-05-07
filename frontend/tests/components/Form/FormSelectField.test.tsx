import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import FormSelectField, { IFormSelectFieldProps } from '../../../src/components/Form/FormSelectField';

const options = [
  { value: 'usa', display: 'United States' },
  { value: 'can', display: 'Canada' },
  { value: 'ger', display: 'Germany' },
];

describe('<FormSelectField />', () => {
  const renderComponent = (props: Partial<IFormSelectFieldProps> = {}) => {
    const handleUpdateField = vi.fn();
    render(
      <FormSelectField
        options={options}
        name="country"
        value=""
        error=""
        label="Country"
        id="country-select"
        errorField="Country"
        handleUpdateField={handleUpdateField}
        {...props}
      />
    );

    return {
      handleUpdateField,
      user: userEvent.setup(),
      select: () => screen.getByTestId('custom-select') as HTMLSelectElement,
      errorMessage: () => screen.queryByText(/country must be/i),
    };
  };

  it('renders with default state', () => {
    const { select } = renderComponent();
    expect(select()).toBeInTheDocument();
    expect(select().value).toBe('usa');
  });

  it('displays options correctly', () => {
    renderComponent();
    expect(screen.getByText('United States')).toBeInTheDocument();
    expect(screen.getByText('Canada')).toBeInTheDocument();
    expect(screen.getByText('Germany')).toBeInTheDocument();
  });

  it('calls handleUpdateField on change', async () => {
    const { select, user, handleUpdateField } = renderComponent();

    await user.selectOptions(select(), 'can');
    expect(handleUpdateField).toHaveBeenCalledWith('country', 'can', 'value');

    await user.selectOptions(select(), 'ger');
    expect(handleUpdateField).toHaveBeenCalledWith('country', 'ger', 'value');
  });

  it('displays error message when error is passed', () => {
    renderComponent({ error: 'Country must be selected' });
    expect(screen.getByText('Country must be selected')).toBeInTheDocument();
  });

  it('does not display error message when there is no error', () => {
    const { errorMessage } = renderComponent();
    expect(errorMessage()).not.toBeInTheDocument();
  });
});
