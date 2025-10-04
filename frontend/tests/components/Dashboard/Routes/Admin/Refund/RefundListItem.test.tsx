import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';
import userEvent from '@testing-library/user-event';
import dayjs from 'dayjs';

import { IRefund } from '../../../../../../src/interfaces';
import { db } from '../../../../../mocks/db';
import RefundListItem from '../../../../../../src/components/Dashboard/Routes/Admin/Refund/RefundListItem';
import { AllProviders } from '../../../../../AllProviders';
import { convertCentsToDollars } from '../../../../../../src/util';

describe('RefundListItem', () => {
  const getProps = () => {
    const refund: IRefund = { ...toPlainObject(db.refund.create()) };

    return { refund };
  };

  const renderComponent = () => {
    const props = getProps();

    render(<RefundListItem {...props} />, { wrapper: AllProviders });

    return {
      props,
      user: userEvent.setup(),
      getModalBtn: () => screen.getByRole('button', { name: /refund details/i }),
    };
  };

  it('should render the refund details correctly', () => {
    const { props } = renderComponent();
    const { fullName, createdAt, amount, status } = props.refund;

    expect(screen.getByText(fullName)).toBeInTheDocument();
    expect(screen.getByText(dayjs(createdAt).format('MM/D/YYYY'))).toBeInTheDocument();
    expect(screen.getByText(new RegExp(convertCentsToDollars(amount).toString(), 'i'))).toBeInTheDocument();
    expect(screen.getByText(new RegExp(status, 'i'))).toBeInTheDocument();
  });

  it('should open modal when "Refund Details" is clicked', async () => {
    const { user, getModalBtn } = renderComponent();

    await user.click(getModalBtn());

    expect(await screen.findByTestId('RefundModalDetails')).toBeInTheDocument();
  });

  it('should close modal when close button inside modal is clicked', async () => {
    const { user, getModalBtn } = renderComponent();

    await user.click(getModalBtn());

    expect(await screen.findByTestId('RefundModalDetails')).toBeInTheDocument();

    await user.click(await screen.findByTestId('refund-modal-close-btn'));

    await waitFor(() => {
      expect(screen.queryByTestId('RefundModalDetails')).not.toBeInTheDocument();
    });
  });
});
