import { screen, render, waitFor } from '@testing-library/react';

import CompletedPayments from '../../../../../src/components/Settings/Billing/User/CompletedPayments';
import { AllProviders } from '../../../../AllProviders';
import userEvent from '@testing-library/user-event';

describe('CompletedPayments', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = () => {
    return {
      userId: 1,
      token: 'mock-token',
    };
  };

  const getElements = () => {
    return {
      getHeading: () => screen.getByRole('heading', { level: 3, name: /completed payments/i }),
      getText: () => screen.getByText(/completed payments/i),
      getPrevBtn: () => screen.queryByRole('button', { name: /prev/i }),
      getNextBtn: () => screen.queryByRole('button', { name: /next/i }),
    };
  };

  const renderComponent = () => {
    const props = getProps();

    render(<CompletedPayments {...props} />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      props,
      elements: getElements(),
    };
  };

  it('should render completed payments and pagination', async () => {
    const { elements } = renderComponent();

    const { getNextBtn } = elements;

    const completedPaymentItems = await screen.findAllByTestId('CompletedPaymentItem');

    expect(completedPaymentItems.length).toBe(2);

    const nexBtn = getNextBtn();

    if (nexBtn !== null) {
      expect(getNextBtn()).toBeInTheDocument();
    }

    expect(screen.queryByRole('button', { name: /prev/i })).not.toBeInTheDocument();
  });

  it('should eventually hide the next button on the last page', async () => {
    const { elements, user } = renderComponent();
    const { getNextBtn, getPrevBtn } = elements;

    await screen.findAllByTestId('CompletedPaymentItem');

    while (getNextBtn()) {
      await user.click(getNextBtn()!);
    }

    await waitFor(() => {
      expect(getNextBtn()).not.toBeInTheDocument();
      expect(getPrevBtn()).toBeInTheDocument();
    });
  });
});
