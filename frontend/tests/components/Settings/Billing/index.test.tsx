import { render, screen, waitFor } from '@testing-library/react';
import { vi } from 'vitest';
import userEvent from '@testing-library/user-event';
import Billing from '../../../../src/components/Settings/Billing';
import { getLoggedInUser } from '../../../utils';
import { HttpResponse, http } from 'msw';
import { baseURL } from '../../../../src/util';
import { IGetPaymentMethodResponse } from '../../../../src/interfaces';
import { server } from '../../../mocks/server';

vi.mock('react-toastify', async () => {
  const actual = await vi.importActual('react-toastify');
  return {
    ...actual,
    toast: { success: vi.fn() },
    ToastContainer: () => <div data-testid="toast-container" />,
  };
});

vi.mock('../../../../src/components/Settings/Billing/NoBillingMethod', () => ({
  default: ({ handleSetView }: { handleSetView: (v: string) => void }) => (
    <div data-testid="NoBillingMethod" onClick={() => handleSetView('add')}>
      NoBillingMethod
    </div>
  ),
}));

vi.mock('../../../../src/components/Settings/Billing/AddBillingMethod', () => ({
  default: ({ handleSetView }: { handleSetView: (v: string) => void }) => (
    <div data-testid="AddBillingMethod" onClick={() => handleSetView('billing')}>
      AddBillingMethod
    </div>
  ),
}));

vi.mock('../../../../src/components/Settings/Billing/BillingForm', () => ({
  default: ({ handleSetView }: { handleSetView: (v: string) => void }) => (
    <div data-testid="BillingForm" onClick={() => handleSetView('main')}>
      BillingForm
    </div>
  ),
}));

vi.mock('../../../../src/components/Settings/Billing/PaymentMethod', () => ({
  default: ({ handleSetStripeEnabled }: { handleSetStripeEnabled: (enabled: boolean) => void }) => (
    <div data-testid="PaymentMethod" onClick={() => handleSetStripeEnabled(false)}>
      PaymentMethod
    </div>
  ),
}));

vi.mock('../../../../src/components/Settings/Billing/User/CompletedPayments', () => ({
  default: () => <div data-testid="CompletedPayments" />,
}));

vi.mock('../../../../src/components/Settings/Billing/Reviewer/ReviewerConnectAccount', () => ({
  default: ({ handleConnectReviewerAccount }: { handleConnectReviewerAccount: () => void }) => (
    <div data-testid="ReviewerConnectAccount" onClick={handleConnectReviewerAccount}>
      ReviewerConnectAccount
    </div>
  ),
}));

vi.mock('../../../../src/components/Settings/Billing/Reviewer/ReviewerDisconnectAccount', () => ({
  default: () => <div data-testid="ReviewerDisconnectAccount" />,
}));

describe('Billing', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const renderComponent = (overrides: Partial<any> = {}) => {
    const { wrapper } = getLoggedInUser({
      role: 'USER',
      ...overrides,
    });

    render(<Billing />, { wrapper });
    return { user: userEvent.setup() };
  };

  it('renders NoBillingMethod by default for USER role', () => {
    renderComponent();
    expect(screen.getByTestId('NoBillingMethod')).toBeInTheDocument();
  });

  it('switches to AddBillingMethod on view change', async () => {
    const { user } = renderComponent({ stripeEnabled: false, role: 'USER' });

    await user.click(screen.getByTestId('NoBillingMethod'));

    await waitFor(() => {
      expect(screen.getByTestId('AddBillingMethod')).toBeInTheDocument();
    });
  });

  it('switches to BillingForm from AddBillingMethod', async () => {
    const { user } = renderComponent();
    await user.click(screen.getByTestId('NoBillingMethod'));
    await user.click(screen.getByTestId('AddBillingMethod'));
    expect(screen.getByTestId('BillingForm')).toBeInTheDocument();
  });

  it('shows PaymentMethod when payment method is enabled', async () => {
    server.use(
      http.get(`${baseURL}/users/:userId/payment-methods`, () => {
        return HttpResponse.json<IGetPaymentMethodResponse>(
          {
            message: 'success',
            data: {
              stripeEnabled: true,
              id: 123,
              last4: '4242',
              displayBrand: 'visa',
              expMonth: 12,
              expYear: 2026,
              name: 'Test User',
            },
          },
          { status: 200 }
        );
      })
    ),
      renderComponent();
    await waitFor(() => {
      expect(screen.getByTestId('PaymentMethod')).toBeInTheDocument();
    });
  });

  it('renders CompletedPayments for USER', () => {
    renderComponent();
    expect(screen.getByTestId('CompletedPayments')).toBeInTheDocument();
  });

  it('shows ReviewerConnectAccount for REVIEWER without stripe connection', async () => {
    server.use(
      http.get(`${baseURL}/users/:userId/payment-methods`, () => {
        return HttpResponse.json<IGetPaymentMethodResponse>(
          {
            message: 'success',
            data: {
              stripeEnabled: true,
              id: 123,
              last4: '4242',
              displayBrand: 'visa',
              expMonth: 12,
              expYear: 2026,
              name: 'Test User',
            },
          },
          { status: 200 }
        );
      })
    ),
      renderComponent({ role: 'REVIEWER' });
    await waitFor(() => {
      expect(screen.getByTestId('ReviewerDisconnectAccount')).toBeInTheDocument();
    });
  });

  it('shows ReviewerDisconnectAccount when reviewer is connected to Stripe', async () => {
    server.use(
      http.get(`${baseURL}/users/:userId/payment-methods`, () => {
        return HttpResponse.json<IGetPaymentMethodResponse>(
          {
            message: 'success',
            data: {
              stripeEnabled: true,
              id: 123,
              last4: '4242',
              displayBrand: 'visa',
              expMonth: 12,
              expYear: 2026,
              name: 'Test User',
            },
          },
          { status: 200 }
        );
      })
    ),
      renderComponent({ role: 'REVIEWER' });
    await waitFor(() => {
      expect(screen.getByTestId('ReviewerDisconnectAccount')).toBeInTheDocument();
    });
  });
});
