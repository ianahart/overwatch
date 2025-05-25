import { screen, render, waitFor } from '@testing-library/react';

import TopicDetails from '../../../src/components/TopicDetails';
import { getLoggedInUser } from '../../utils';
import userEvent from '@testing-library/user-event';
import { mockNavigate, setMockParams } from '../../setup';

describe('TopicDetails', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    setMockParams({ topicId: '1' });
  });

  const renderComponent = () => {
    const { wrapper, curUser } = getLoggedInUser({ id: 0 });

    render(<TopicDetails />, { wrapper });

    return {
      user: userEvent.setup(),
      curUser,
    };
  };

  it('should render loading state initially', () => {
    renderComponent();

    expect(screen.getByText(/loading topic/i)).toBeInTheDocument();
  });

  it('redirects to signin when unauthenticated user clicks "Add a comment"', async () => {
    const { user } = renderComponent();

    await user.click(screen.getByRole('button', { name: /Add a comment/i }));

    await waitFor(() => {
      expect(mockNavigate).toHaveBeenCalledWith('/signin');
    });
  });
});
