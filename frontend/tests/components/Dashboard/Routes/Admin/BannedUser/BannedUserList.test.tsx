import { screen, render } from '@testing-library/react';
import BannedUserList from '../../../../../../src/components/Dashboard/Routes/Admin/BannedUser/BannedUserList';
import { getLoggedInUser } from '../../../../../utils';
import userEvent from '@testing-library/user-event';

describe('BannedUserList', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = () => {
    return {
      handleSetView: vi.fn(),
    };
  };

  const renderComponent = () => {
    const props = getProps();
    const { wrapper } = getLoggedInUser();

    render(<BannedUserList {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      props,
    };
  };

  it('should render heading and initial user', async () => {
    renderComponent();

    expect(screen.getByRole('heading', { name: /banned users/i })).toBeInTheDocument();
    const bannedUsers = await screen.findAllByTestId('BannedUserListItem');
    expect(bannedUsers).toHaveLength(2);
    expect(await screen.findByRole('button', { name: /next/i })).toBeInTheDocument();
  });

  it('should paginate when the next button is clicked', async () => {
    const { user } = renderComponent();

    const nextBtn = await screen.findByRole('button', { name: /next/i });

    await user.click(nextBtn);

    expect(await screen.findByText(/2 of 10/i)).toBeInTheDocument();
  });

  it('should paginate when the prev button is clicked', async () => {
    const { user } = renderComponent();

    const nextBtn = await screen.findByRole('button', { name: /next/i });
    await user.click(nextBtn);

    const prevBtn = await screen.findByRole('button', { name: /prev/i });

    expect(await screen.findByText(/2 of 10/i)).toBeInTheDocument();
    await user.click(prevBtn);
    expect(await screen.findByText(/1 of 10/i)).toBeInTheDocument();
  });
});
