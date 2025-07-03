import { screen, render, waitFor } from '@testing-library/react';

import ReviewerDisconnectAccount from '../../../../../src/components/Settings/Billing/Reviewer/ReviewerDisconnectAccount';
import { getLoggedInUser } from '../../../../utils';
import userEvent from '@testing-library/user-event';

describe('ReviewerDisconnectAccount', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = () => {
    return {
      handleSetStripeEnabled: vi.fn(),
      paymentMethodId: 1,
    };
  };

  const getElements = () => {
    return {
      getHeading: () => screen.getByRole('heading', { level: 3, name: /stripe account/i }),
      getText: () => screen.getByText(/negative account balances/i),
      getStripeLink: () => screen.getByRole('link', { name: /stripe/i }),
      getDisconnectBtn: () => screen.getByRole('button', { name: /disconnect account/i }),
    };
  };

  const renderComponent = () => {
    const props = getProps();

    const { wrapper } = getLoggedInUser();

    render(<ReviewerDisconnectAccount {...props} />, { wrapper });

    return {
      props,
      user: userEvent.setup(),
      elements: getElements(),
    };
  };

  it('should render heading, text, stripe link, and disconnect button', () => {
    const { elements } = renderComponent();

    const { getText, getHeading, getStripeLink, getDisconnectBtn } = elements;

    expect(getText()).toBeInTheDocument();
    expect(getHeading()).toBeInTheDocument();
    expect(getStripeLink()).toBeInTheDocument();
    expect(getDisconnectBtn()).toBeInTheDocument();
  });

  it('should have link to stripe', () => {
    const { elements } = renderComponent();

    expect(elements.getStripeLink()).toHaveAttribute('href', 'https://stripe.com/');
  });

  it('should call "disconnectAccount" when disconnect button is clicked', async () => {
    const { user, elements, props } = renderComponent();

    await user.click(elements.getDisconnectBtn());

    await waitFor(() => {
      expect(props.handleSetStripeEnabled).toHaveBeenCalledWith(false);
    });
  });
});
