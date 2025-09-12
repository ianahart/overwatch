import { screen, render, waitFor } from '@testing-library/react';
import { getLoggedInUser } from '../../../../../utils';
import BadgeList from '../../../../../../src/components/Dashboard/Routes/Admin/Badge/BadgeList';

describe('BadgeList', () => {
  const renderComponent = () => {
    const { wrapper } = getLoggedInUser();

    render(<BadgeList />, { wrapper });

    return {
      getNextBtn: () => screen.getByRole('button', { name: /next/i }),
      getPrevBtn: () => screen.getByRole('button', { name: /prev/i }),
    };
  };

  it('should render badges from the query', async () => {
    renderComponent();

    const badges = await screen.findAllByTestId('BadgeListItem');

    expect(badges).toHaveLength(2);
  });

  it('should show next page button when there are more pages', async () => {
    const { getNextBtn } = renderComponent();

    await waitFor(() => {
      expect(getNextBtn()).toBeInTheDocument();
      expect(screen.getByText('1')).toBeInTheDocument();
    });
  });
});
