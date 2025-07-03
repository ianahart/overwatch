import { screen, render, waitFor } from '@testing-library/react';

import ReviewerConnectAccount from '../../../../../src/components/Settings/Billing/Reviewer/ReviewerConnectAccount';
import { AllProviders } from '../../../../AllProviders';
import userEvent from '@testing-library/user-event';

describe('ReviewerConnectAccount', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = () => {
    return {
      handleConnectReviewerAccount: vi.fn(),
    };
  };

  const getElements = () => {
    return {
      getHeading: () => screen.getByRole('heading', { level: 3, name: /connect your account/i }),
      getText: () => screen.getByText(/you need to connect/i),
      getConnectBtn: () => screen.getByRole('button', { name: /connect your account/i }),
      getStripeLink: () => screen.getByRole('link', { name: /stripe/i }),
    };
  };

  const renderComponent = () => {
    const props = getProps();

    render(<ReviewerConnectAccount {...props} />, { wrapper: AllProviders });

    return {
      props,
      user: userEvent.setup(),
      elements: getElements(),
    };
  };

  it('should render heading, button, and text content', () => {
    const { elements } = renderComponent();

    const { getText, getHeading, getConnectBtn } = elements;

    expect(getHeading()).toBeInTheDocument();
    expect(getText()).toBeInTheDocument();
    expect(getConnectBtn()).toBeInTheDocument();
  });

  it('should contain a link to Stripe', () => {
    const { elements } = renderComponent();

    expect(elements.getStripeLink()).toBeInTheDocument();
    expect(elements.getStripeLink()).toHaveAttribute('href', 'https://stripe.com/');
  });

  it('should call "handleConnectReviewerAccount" when button is clicked', async () => {
    const { user, props, elements } = renderComponent();

    await user.click(elements.getConnectBtn());

    await waitFor(() => {
      expect(props.handleConnectReviewerAccount).toHaveBeenCalled();
    });
  });
});
