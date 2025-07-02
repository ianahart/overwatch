import { screen, render, waitFor } from '@testing-library/react';
import { vi, Mock } from 'vitest';
import { toast } from 'react-toastify';

import BillingForm from '../../../../src/components/Settings/Billing/BillingForm';
import { getLoggedInUser } from '../../../utils';
import userEvent from '@testing-library/user-event';
import { useElements, useStripe } from '@stripe/react-stripe-js';

vi.mock('react-toastify', async () => {
  const actual = await vi.importActual<typeof import('react-toastify')>('react-toastify');
  return {
    ...actual,
    toast: {
      ...actual.toast,
      success: vi.fn(),
      error: vi.fn(),
    },
  };
});

vi.mock('@stripe/react-stripe-js', async () => {
  const actual = await vi.importActual<typeof import('@stripe/react-stripe-js')>('@stripe/react-stripe-js');
  return {
    ...actual,
    useStripe: vi.fn(),
    useElements: vi.fn(),
    CardElement: vi.fn(() => <div data-testid="CardElement" />),
  };
});

const mockCreatePaymentMethod = vi.fn();

const mockStripe = {
  createPaymentMethod: vi.fn(),
};
(useStripe as Mock).mockReturnValue(mockStripe);

const mockElements = {
  getElement: vi.fn(),
};
(useElements as Mock).mockReturnValue(mockElements);

describe('BillingForm', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getStripeResponse = () => {
    return {
      paymentMethod: {
        id: 'pm_test_123',
        type: 'card',
        billing_details: {
          name: 'John Doe',
          address: {
            city: 'Seattle',
            country: 'US',
            line1: '123 Main St',
            line2: '',
            postal_code: '98101',
          },
        },
        card: {
          brand: 'visa',
          exp_month: 12,
          exp_year: 2026,
        },
      },
    };
  };

  const getProps = () => {
    return {
      handleSetView: vi.fn(),
    };
  };

  const getForm = () => {
    return {
      getHeading: () => screen.getByText(/add a billing method/i),
      getCardElement: () => screen.getByTestId('CardElement'),
      getSaveBtn: () => screen.getByRole('button', { name: /save payment method/i }),
    };
  };

  const renderComponent = () => {
    const props = getProps();

    const { wrapper } = getLoggedInUser();

    render(<BillingForm {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      form: getForm(),
      props,
      stripeResponse: getStripeResponse(),
    };
  };

  it('should render form inputs and stripe card element', () => {
    const { form } = renderComponent();

    const { getHeading, getSaveBtn, getCardElement } = form;

    expect(getHeading()).toBeInTheDocument();
    expect(getCardElement()).toBeInTheDocument();
    expect(getSaveBtn()).toBeInTheDocument();
  });

  it('should show validation errors if required fields are empty', async () => {
    const { user, form } = renderComponent();

    const { getSaveBtn } = form;

    await user.click(getSaveBtn());

    await waitFor(() => {
      const errors = screen.getAllByText(/please make sure to fill out field/i);
      expect(errors.length).toBe(6);
    });
    expect(mockStripe.createPaymentMethod).not.toHaveBeenCalled();
  });

  it('should submit form and call stripe and API on success', async () => {
    const { user, form, stripeResponse } = renderComponent();

    const { getSaveBtn } = form;

    mockStripe.createPaymentMethod.mockResolvedValue(stripeResponse);
    mockElements.getElement.mockReturnValue({}); // mock CardElement
    mockCreatePaymentMethod.mockReturnValue({ unwrap: () => Promise.resolve({}) });

    await user.type(screen.getByLabelText(/First Name/i), 'John');
    await user.type(screen.getByLabelText(/Last Name/i), 'Doe');
    await user.type(screen.getByLabelText(/Address line 1/i), '123 Main St');
    await user.type(screen.getByLabelText(/City/i), 'Seattle');
    await user.type(screen.getByLabelText(/State/i), 'WA');
    await user.type(screen.getByLabelText(/Postal code/i), '98101');

    await user.click(getSaveBtn());

    await waitFor(() => {
      expect(toast.success).toHaveBeenCalledWith(
        'Your payment info was successfully saved!',
        expect.objectContaining({
          position: 'bottom-center',
          autoClose: 5000,
          theme: 'dark',
        })
      );
    });
  });
});
