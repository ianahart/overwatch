import { screen, render, waitFor } from '@testing-library/react';

import PaymentRefundModal from '../../../../../src/components/Settings/Billing/User/PaymentRefundModal';
import { getLoggedInUser } from '../../../../utils';
import userEvent from '@testing-library/user-event';
import { server } from '../../../../mocks/server';
import { HttpResponse, http } from 'msw';
import { baseURL } from '../../../../../src/util';

describe('PaymentRefundModal', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = () => {
    return {
      handleCloseModal: vi.fn(),
      stripePaymentIntentId: 1,
      handleUpdateStatus: vi.fn(),
    };
  };

  const getElements = () => {
    return {
      getCloseModalBtn: () => screen.getByTestId('refund-modal-close'),
      getTextarea: () => screen.getByLabelText(/reason for refund/i),
      getSubmitBtn: () => screen.getByRole('button', { name: /submit/i }),
      getCancelBtn: () => screen.getByRole('button', { name: /cancel/i }),
    };
  };

  const renderComponent = () => {
    const props = getProps();

    const { curUser, wrapper } = getLoggedInUser();

    render(<PaymentRefundModal {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      curUser,
      props,
      elements: getElements(),
    };
  };

  it('should render UI elements', () => {
    const { elements } = renderComponent();

    const { getTextarea, getCancelBtn, getSubmitBtn, getCloseModalBtn } = elements;

    expect(getTextarea()).toBeInTheDocument();
    expect(getCancelBtn()).toBeInTheDocument();
    expect(getSubmitBtn()).toBeInTheDocument();
    expect(getCloseModalBtn()).toBeInTheDocument();
  });

  it('should validate empty reason input', async () => {
    const MAX_CONTENT_LENGTH = 1000;
    const { user, elements } = renderComponent();

    await user.click(elements.getSubmitBtn());

    expect(
      await screen.findByText(`Reason must be between 1 and ${MAX_CONTENT_LENGTH} characters`)
    ).toBeInTheDocument();
  });

  it('should validate excessively long reason input', async () => {
    const MAX_CONTENT_LENGTH = 1000;
    const { user, elements } = renderComponent();

    const { getTextarea, getSubmitBtn } = elements;

    await user.type(getTextarea(), 'a'.repeat(1001));
    await user.click(getSubmitBtn());

    expect(
      await screen.findByText(`Reason must be between 1 and ${MAX_CONTENT_LENGTH} characters`)
    ).toBeInTheDocument();
  });

  it('should call mutation and close modal on successful submit', async () => {
    const { user, props, elements } = renderComponent();

    const { getTextarea, getSubmitBtn } = elements;

    await user.type(getTextarea(), 'test reason');
    await user.click(getSubmitBtn());

    await waitFor(() => {
      expect(props.handleCloseModal).toHaveBeenCalled();
    });
  });

  it('should show server errors on an unsuccessful submit', async () => {
    server.use(
      http.post(`${baseURL}/payment-refunds`, () => {
        return HttpResponse.json(
          {
            message: 'invalid refund reason',
          },
          { status: 400 }
        );
      })
    );

    const { user, elements } = renderComponent();

    const { getTextarea, getSubmitBtn } = elements;

    await user.type(getTextarea(), 'test reason');
    await user.click(getSubmitBtn());

    expect(await screen.findByText(/invalid refund reason/i)).toBeInTheDocument();
  });

  it('should call "handleCloseModal" when cancel is clicked', async () => {
    const { user, elements, props } = renderComponent();

    await user.click(elements.getCancelBtn());

    await waitFor(() => {
      expect(props.handleCloseModal).toHaveBeenCalled();
    });
  });
});
