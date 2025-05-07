import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import FormSelect, { IFormSelectProps } from '../../../src/components/Form/FormSelect';

const data = [
  { name: 'United States', value: 'United States', id: 1 },
  { name: 'Canada', value: 'Canada', id: 2 },
  { name: 'Germany', value: 'Germany', id: 3 },
];

describe('<FormSelect />', () => {
  const renderComponent = (props: Partial<IFormSelectProps> = {}, countryText: string) => {
    const updateField = vi.fn();
    render(<FormSelect data={data} country="United States" updateField={updateField} {...props} />);
    return {
      updateField,
      user: userEvent.setup(),
      countryText: screen.getByText(countryText),
      getInput: () => screen.getByPlaceholderText('Search...'),
    };
  };

  it('should render with default selected country', () => {
    const { countryText } = renderComponent({}, 'United States');
    expect(countryText).toBeInTheDocument();
  });

  it('should open dropdown on click', async () => {
    const { user, countryText, getInput } = renderComponent({}, 'United States');

    await user.click(countryText);
    expect(getInput()).toBeInTheDocument();
    expect(screen.getByText('Canada')).toBeInTheDocument();
    expect(screen.getByText('Germany')).toBeInTheDocument();
  });

  it('should filter countries based on input', async () => {
    const { getInput, user, countryText } = renderComponent({}, 'United States');

    await user.click(countryText);
    await user.type(getInput(), 'can');

    expect(await screen.findByText('Canada')).toBeInTheDocument();
    expect(screen.queryByText('Germany')).not.toBeInTheDocument();
  });

  it('should select a country and calls updateField', async () => {
    const { user, countryText, updateField } = renderComponent({}, 'United States');

    await user.click(countryText);
    const option = screen.getByText('Germany');
    await user.click(option);

    expect(updateField).toHaveBeenCalledWith('country', 'Germany');
  });

  it('should close dropdown when clicking outside', async () => {
    const { user, countryText } = renderComponent({}, 'United States');

    await user.click(countryText);
    expect(screen.getByText('Canada')).toBeInTheDocument();

    await user.click(document.body);
    expect(screen.queryByText('Canada')).not.toBeInTheDocument();
  });
});
