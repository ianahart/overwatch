import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';
import { HttpResponse, http } from 'msw';
import userEvent from '@testing-library/user-event';

import { IRefund } from '../../../../../../src/interfaces';
import { db } from '../../../../../mocks/db';
import RefundModal from '../../../../../../src/components/Dashboard/Routes/Admin/Refund/RefundModalDetails';
import { getLoggedInUser } from '../../../../../utils';
import { server } from '../../../../../mocks/server';
import { baseURL } from '../../../../../../src/util';

describe('RefundModalDetails', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getElements = () => {
    return {
      getModalCloseBtn: () => screen.getByTestId('refund-modal-close-btn'),
      getHeading: () => screen.getByRole('heading', { name: /reason for refund/i }),
      getTextarea: () => screen.getByLabelText(/admin notes/i),
      getApproveInput: () => screen.getByLabelText(/approve/i),
      getRejectInput: () => screen.getByLabelText(/reject/i),
      getRefundBtn: () => screen.getByRole('button', { name: /refund/i }),
      getCancelBtn: () => screen.getByRole('button', { name: /cancel/i }),
    };
  };

  const getProps = (overrides: Partial<IRefund> = {}) => {
    const refund: IRefund = { ...toPlainObject(db.refund.create()), ...overrides };
    const handleCloseModal = vi.fn();

    return { refund, handleCloseModal };
  };

  const renderComponent = (overrides: Partial<IRefund> = {}) => {
    const props = getProps(overrides);

    const { wrapper } = getLoggedInUser();

    render(<RefundModal {...props} />, { wrapper });

    return {
      props,
      user: userEvent.setup(),
      elements: getElements(),
    };
  };

  it('should render refund details', () => {
    const { props } = renderComponent();

    const { reason, fullName } = props.refund;

    expect(screen.getByText(reason)).toBeInTheDocument();
    expect(screen.getByText(`From: ${fullName}`)).toBeInTheDocument();
  });

  it('should update notes and approval status', async () => {
    const { user, elements } = renderComponent();

    const { getApproveInput, getTextarea } = elements;

    await user.type(getTextarea(), 'some admin notes');
    await user.click(getApproveInput());

    expect(getTextarea()).toHaveValue('some admin notes');
    expect(getApproveInput()).toBeChecked();
  });

  it('should show error if notes are empty and submit is clicked', async () => {
    const { user, elements } = renderComponent();
    const MAX_CONTENT_LENGTH = 300;

    const { getRefundBtn } = elements;
    await user.click(getRefundBtn());

    expect(
      await screen.findByText(`Admin notes must be between 1 and ${MAX_CONTENT_LENGTH} characters`)
    ).toBeInTheDocument();
  });

  it('should call "updatePaymentRefund" on valid submit', async () => {
    const { user, props, elements } = renderComponent();

    const { getApproveInput, getTextarea, getRefundBtn } = elements;

    await user.type(getTextarea(), 'some admin notes');
    await user.click(getApproveInput());
    await user.click(getRefundBtn());

    await waitFor(() => {
      expect(props.handleCloseModal).toHaveBeenCalled();
    });
  });

  it('should apply API errors on faulty submit', async () => {
    server.use(
      http.patch(`${baseURL}/admin/:userId/payment-refunds/:id`, () => {
        return HttpResponse.json(
          {
            message: 'Please make sure you filled the form correctly',
          },
          { status: 400 }
        );
      })
    );

    const { user, elements } = renderComponent();

    const { getApproveInput, getTextarea, getRefundBtn } = elements;

    await user.type(getTextarea(), 'some admin notes');
    await user.click(getApproveInput());
    await user.click(getRefundBtn());

    expect(await screen.findByText(/please make sure you filled the form correctly/i)).toBeInTheDocument();
  });

  it('should call "handleCloseModal" when "Cancel" button is clicked', async () => {
    const { user, props, elements } = renderComponent();

    await user.click(elements.getCancelBtn());

    await waitFor(() => {
      expect(props.handleCloseModal).toHaveBeenCalled();
    });
  });
});
