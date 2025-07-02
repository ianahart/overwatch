import { screen, render, waitFor } from '@testing-library/react';

import NoBillingMethod from '../../../../src/components/Settings/Billing/NoBillingMethod';
import { AllProviders } from '../../../AllProviders';
import userEvent from '@testing-library/user-event';

describe('NoBillingMethod', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = () => {
    return {
      handleSetView: vi.fn(),
    };
  };

  const getElements = () => {
    return {
      getAddPaymentMethodContainer: () => screen.getByTestId('setting-billing-add-payment-method'),
      getHeading: () => screen.getByRole('heading', { name: /billing methods/i }),
    };
  };

  const renderComponent = () => {
    const props = getProps();

    render(<NoBillingMethod {...props} />, { wrapper: AllProviders });

    return {
      props,
      user: userEvent.setup(),
      elements: getElements(),
    };
  };

  it('should render the heading and add payment icon', () => {
    const { elements } = renderComponent();

    const { getHeading, getAddPaymentMethodContainer } = elements;

    expect(getHeading()).toBeInTheDocument();
    expect(getAddPaymentMethodContainer()).toBeInTheDocument();
  });

  it('should call "handleSetView("add")" when payment method container is clicked', async () => {
    const { user, props, elements } = renderComponent();

    await user.click(elements.getAddPaymentMethodContainer());

    await waitFor(() => {
      expect(props.handleSetView).toHaveBeenCalledWith('add');
    });
  });
});
