import { screen, render, waitFor } from '@testing-library/react';

import PaymentMethod from '../../../../src/components/Settings/Billing/PaymentMethod';
import { getLoggedInUser } from '../../../utils';
import userEvent from '@testing-library/user-event';

describe('PaymentMethod', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = () => {
    const data = {
      id: 1,
      last4: '4242',
      displayBrand: 'visa',
      expYear: 2026,
      expMonth: 12,
      name: 'Test User',
    };
    const mockSetStripeEnabled = vi.fn();

    return {
      data,
      handleSetStripeEnabled: mockSetStripeEnabled,
    };
  };

  const getForm = () => {
    return {
      getHeading: () => screen.getByText(/your payment method/i),
      getName: () => screen.getByText(/test user/i),
      getLastFour: () => screen.getByText(/\*{12}4242/),
      getExp: () => screen.getByText(/Expiration date: 12\/2026/),
    };
  };

  const renderComponent = () => {
    const { wrapper } = getLoggedInUser();

    const props = getProps();
    const form = getForm();

    render(<PaymentMethod {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      form,
      props,
    };
  };

  it('should render user card details correctly', () => {
    const { form } = renderComponent();

    const { getHeading, getExp, getName, getLastFour } = form;

    expect(getHeading()).toBeInTheDocument();
    expect(getExp()).toBeInTheDocument();
    expect(getName()).toBeInTheDocument();
    expect(getLastFour()).toBeInTheDocument();
  });

  it('should call "deletePaymentMethod" and disable Stripe when trash icon is clicked', async () => {
    const { user, props } = renderComponent();

    const deleteIcon = screen.getByTestId('payment-method-delete-icon');

    await user.click(deleteIcon);

    await waitFor(() => {
      expect(props.handleSetStripeEnabled).toHaveBeenCalledWith(false);
    });
  });
});
