import { screen, render, waitFor } from '@testing-library/react';

import AddBillingMethod from '../../../../src/components/Settings/Billing/AddBillingMethod';
import { AllProviders } from '../../../AllProviders';
import userEvent from '@testing-library/user-event';

describe('AddBillingMethod', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getElements = () => {
    return {
      getHeading: () => screen.getByRole('heading', { level: 3, name: /add a billing method/i }),
      getCancelBtn: () => screen.getByRole('button', { name: /cancel/i }),
      getInput: () => screen.getByRole('radio'),
    };
  };

  const getProps = () => {
    return {
      handleSetView: vi.fn(),
    };
  };

  const renderComponent = () => {
    const props = getProps();
    const elements = getElements();

    render(<AddBillingMethod {...props} />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      props,
      elements,
    };
  };

  it('should render the heading and cancel btn', () => {
    const { elements } = renderComponent();

    expect(elements.getHeading()).toBeInTheDocument();
    expect(elements.getCancelBtn()).toBeInTheDocument();
  });

  it('should call "handleSetView("main")" when cancel btn is clicked', async () => {
    const { user, elements, props } = renderComponent();

    await user.click(elements.getCancelBtn());

    await waitFor(() => {
      expect(props.handleSetView).toHaveBeenCalledWith('main');
    });
  });

  it('should call "handleSetView("billing")" when user is typing into input', async () => {
    const { user, elements, props } = renderComponent();

    await user.type(elements.getInput(), 'next');

    await waitFor(() => {
      expect(props.handleSetView).toHaveBeenCalledWith('billing');
    });
  });
});
