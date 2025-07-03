import { screen, render, waitFor } from '@testing-library/react';

import CompletedPaymentItem from '../../../../../src/components/Settings/Billing/User/CompletedPaymentItem';
import { IPaymentIntent } from '../../../../../src/interfaces';
import { AllProviders } from '../../../../AllProviders';
import userEvent from '@testing-library/user-event';
import { convertCentsToDollars } from '../../../../../src/util';

describe('CompletedPaymentItem', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = (overrides = {}) => {
    const paymentIntent: IPaymentIntent = {
      id: 1,
      amount: 1000,
      currency: 'USD',
      fullName: 'John Doe',
      reviewerId: 2,
      avatarUrl: 'https://imgur.com/photo',
      createdAt: new Date().toString(),
      status: 'PAID',
      ...overrides,
    };

    return {
      paymentIntent,
      handleUpdateStatus: vi.fn(),
    };
  };

  const getElements = (paymentIntent: IPaymentIntent) => {
    return {
      getName: () => screen.getByText(paymentIntent.fullName),
      getDate: () => screen.getByText(/Paid on:/i),
      getStatus: () => screen.getByText(/status:/i),
      getAmount: () => screen.getByText(`$-${convertCentsToDollars(paymentIntent.amount)}`),
      getCurrency: () => screen.getByText(paymentIntent.currency),
      getRefundBtn: () => screen.getByRole('button', { name: /ask for refund/i }),
    };
  };

  const renderComponent = (overrides = {}) => {
    const props = getProps(overrides);
    const elements = getElements(props.paymentIntent);

    render(<CompletedPaymentItem {...props} />, { wrapper: AllProviders });

    return {
      props,
      user: userEvent.setup(),
      elements,
    };
  };

  it('should render user name, date, status, and amount correctly', () => {
    const { elements } = renderComponent();

    const { getDate, getName, getAmount, getStatus, getCurrency } = elements;

    expect(getDate()).toBeInTheDocument();
    expect(getName()).toBeInTheDocument();
    expect(getAmount()).toBeInTheDocument();
    expect(getStatus()).toBeInTheDocument();
    expect(getCurrency()).toBeInTheDocument();
  });

  it('should render the refund button if not refeund', () => {
    const { elements } = renderComponent();

    expect(elements.getRefundBtn()).toBeInTheDocument();
  });

  it('should NOT render the refund button if status is "REFUNDED"', () => {
    renderComponent({ status: 'REFUNDED' });

    expect(screen.queryByRole('button', { name: /ask for refund/i })).not.toBeInTheDocument();
  });

  it('should open and close modal', async () => {
    const { user, elements } = renderComponent();

    expect(screen.queryByTestId('PaymentRefundModal')).not.toBeInTheDocument();

    await user.click(elements.getRefundBtn());

    expect(await screen.findByTestId('PaymentRefundModal')).toBeInTheDocument();

    await user.click(screen.getByTestId('refund-modal-close'));

    await waitFor(() => {
      expect(screen.queryByTestId('PaymentRefundModal')).not.toBeInTheDocument();
    });
  });
});
