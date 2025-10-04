import { screen, render } from '@testing-library/react';
import RefundList from '../../../../../../src/components/Dashboard/Routes/Admin/Refund/RefundList';
import { getLoggedInUser } from '../../../../../utils';
import userEvent from '@testing-library/user-event';

describe('RefundList', () => {
  const renderComponent = () => {
    const { wrapper } = getLoggedInUser();

    render(<RefundList />, { wrapper });

    return {
      user: userEvent.setup(),
      getPrevBtn: () => screen.findByRole('button', { name: /prev/i }),
      getNextBtn: () => screen.findByRole('button', { name: /next/i }),
    };
  };

  it('should render the RefundListItem(s) on render', async () => {
    renderComponent();
    const size = 2;

    const refunds = await screen.findAllByTestId('RefundListItem');

    expect(refunds).toHaveLength(size);
  });

  it('should paginate refunds when clicking the "Next" button', async () => {
    const { user, getNextBtn } = renderComponent();

    await user.click(await getNextBtn());

    expect(await screen.findByText('2')).toBeInTheDocument();
  });

  it('should paginate refunds when clicking the "Prev" button', async () => {
    const { user, getNextBtn, getPrevBtn } = renderComponent();

    await user.click(await getNextBtn());

    expect(await screen.findByText('2')).toBeInTheDocument();

    await user.click(await getPrevBtn());

    expect(await screen.findByText('1')).toBeInTheDocument();
  });
});
