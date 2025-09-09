import { screen, render } from '@testing-library/react';
import { getLoggedInUser } from '../../../../../utils';
import AllUser from '../../../../../../src/components/Dashboard/Routes/Admin/AllUser';
import userEvent from '@testing-library/user-event';

describe('AllUser', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const renderComponent = () => {
    const { wrapper } = getLoggedInUser();

    render(<AllUser />, { wrapper });

    return {
      user: userEvent.setup(),
    };
  };

  it('should load and display users from API', async () => {
    renderComponent();

    const users = await screen.findAllByTestId('UserListItem');

    expect(users).toHaveLength(2);
  });

  it('should show next button if there are more pages', async () => {
    renderComponent();

    const nextBtn = await screen.findByRole('button', { name: /next/i });

    expect(nextBtn).toBeInTheDocument();
  });

  it('should navigate pages when next button is clicked', async () => {
    const { user } = renderComponent();

    const nextBtn = await screen.findByRole('button', { name: /next/i });

    await user.click(nextBtn);

    expect(screen.getByText('2')).toBeInTheDocument();
  });
});
