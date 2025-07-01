import { screen, render, waitFor } from '@testing-library/react';

import LocationForm from '../../../../src/components/Settings/ContactInfo/LocationForm';
import { ILocationForm } from '../../../../src/interfaces';
import { getLoggedInUser } from '../../../utils';
import userEvent from '@testing-library/user-event';

describe('LocationForm', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = (overrides = {}) => {
    const form: ILocationForm = {
      address: { name: 'address', error: '', value: '', type: 'text' },
      addressTwo: { name: 'addressTwo', error: '', value: '', type: 'text' },
      city: { name: 'city', error: '', value: '', type: 'text' },
      country: { name: 'country', error: '', value: 'United States', type: 'text' },
      phoneNumber: { name: 'phoneNumber', error: '', value: '', type: 'text' },
      state: { name: 'state', error: '', value: '', type: 'text' },
      zipCode: { name: 'zipCode', error: '', value: '', type: 'text' },
    };

    return {
      form,
      error: '',
      closeForm: vi.fn(),
      handleUpdateField: vi.fn(),
      handleOnSubmit: vi.fn(),
      ...overrides,
    };
  };

  const getIcons = () => {
    return {
      getLocationOpenIcon: () => screen.getByTestId('location-chevron-left'),
      getLocationCloseIcon: () => screen.getByTestId('location-chevron-down'),
    };
  };

  const getForm = () => {
    return {
      getAddressInput: () => screen.getByLabelText(/address/i),
      getAddressTwoInput: () => screen.getByPlaceholderText('Apt/Suite'),
      getCityInput: () => screen.getByLabelText(/city/i),
      getStateInput: () => screen.getByLabelText(/state/i),
      getZipInput: () => screen.getByPlaceholderText(/zip/i),
      getUpdateBtn: () => screen.getByRole('button', { name: /update/i }),
      getCancelBtn: () => screen.getByRole('button', { name: /cancel/i }),
    };
  };

  const renderComponent = (overrides = {}) => {
    const props = getProps(overrides);

    const { wrapper } = getLoggedInUser();

    render(<LocationForm {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      icons: getIcons(),
      props,
      form: getForm(),
    };
  };

  it('should render inputs and buttons', () => {
    const { form } = renderComponent();

    const {
      getZipInput,
      getCancelBtn,
      getCityInput,
      getUpdateBtn,
      getStateInput,
      getAddressInput,
      getAddressTwoInput,
    } = form;

    expect(getZipInput()).toBeInTheDocument();
    expect(getCancelBtn()).toBeInTheDocument();
    expect(getCityInput()).toBeInTheDocument();
    expect(getUpdateBtn()).toBeInTheDocument();
    expect(getStateInput()).toBeInTheDocument();
    expect(getAddressInput()).toBeInTheDocument();
    expect(getAddressTwoInput()).toBeInTheDocument();
  });

  it('should render error message if provided as a prop', () => {
    renderComponent({ error: 'this is an error' });

    expect(screen.getByText('this is an error')).toBeInTheDocument();
  });

  it('should call "handleOnSubmit" on form submit', async () => {
    const { user, form, props } = renderComponent();

    await user.click(form.getUpdateBtn());

    await waitFor(() => {
      expect(props.handleOnSubmit).toHaveBeenCalled();
    });
  });

  it('should call "closeForm" on cancel button click', async () => {
    const { user, form, props } = renderComponent();

    await user.click(form.getCancelBtn());

    await waitFor(() => {
      expect(props.closeForm).toHaveBeenCalled();
    });
  });

  it('should display dropdown results when address results are fetched', async () => {
    const { user } = renderComponent();

    const input = screen.getByPlaceholderText(/start typing your address/i);
    await user.type(input, '123 Main St');

    expect(await screen.findByText(/123 Main St/i)).toBeInTheDocument();
    expect(await screen.findByText(/Springfield, IL/i)).toBeInTheDocument();
  });
});
