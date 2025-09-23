import { screen, render } from '@testing-library/react';
import FlaggedCommentList from '../../../../../../src/components/Dashboard/Routes/Admin/FlaggedComment/FlaggedCommentList';
import { getLoggedInUser } from '../../../../../utils';
import userEvent from '@testing-library/user-event';

describe('FlaggedCommentList', () => {
  const renderComponent = () => {
    const { wrapper } = getLoggedInUser();
    render(<FlaggedCommentList />, { wrapper });

    return {
      user: userEvent.setup(),
      getNextBtn: () => screen.getByRole('button', { name: /next/i }),
    };
  };

  it('should render a list of flagged comments', async () => {
    renderComponent();

    const flaggedComments = await screen.findAllByTestId('FlaggedCommentListItem');

    expect(flaggedComments).toHaveLength(2);
  });

  it('should paginate the flagged comments', async () => {
    const { user, getNextBtn } = renderComponent();
    await screen.findAllByTestId('FlaggedCommentListItem');

    await user.click(getNextBtn());

    expect(screen.getByRole('button', { name: /prev/i })).toBeInTheDocument();
  });
});
