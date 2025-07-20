import { screen, render } from '@testing-library/react';
import userEvent from '@testing-library/user-event';

import BadgeList from '../../../../../../src/components/Dashboard/Routes/Reviewer/Badge/BadgeList';
import { getLoggedInUser } from '../../../../../utils';

describe('BadgeList', () => {
  const renderComponent = () => {
    const { wrapper } = getLoggedInUser();

    render(<BadgeList />, { wrapper });

    return {
      user: userEvent.setup(),
      getNextBtn: () => screen.findByRole('button', { name: /next/i }),
    };
  };

  it('should render badges on initial load', async () => {
    renderComponent();

    const badges = await screen.findAllByTestId('reviewer-badge-list-item');

    expect(badges.length).toBe(2);
  });

  it('should navigate to the next page on button click', async () => {
    const { getNextBtn, user } = renderComponent();

    await user.click(await getNextBtn());

    const badges = await screen.findAllByTestId('reviewer-badge-list-item');

    expect(badges.length).toBe(2);

    expect(await screen.findByText('2')).toBeInTheDocument();
  });
});
