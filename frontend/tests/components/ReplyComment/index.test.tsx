import { screen, render, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';

import ReplyComment from '../../../src/components/ReplyComment';
import { mockUserSearchParams, setMockParams } from '../../setup';
import { getLoggedInUser } from '../../utils';

describe('ReplyComment', () => {
  beforeEach(() => {
    vi.clearAllMocks();

    setMockParams({ commentId: '1' });
    mockUserSearchParams({ sender: '1' });
  });

  const renderComponent = () => {
    const { wrapper, curUser } = getLoggedInUser();

    render(<ReplyComment />, { wrapper });

    return {
      getReplyComments: () => screen.findAllByTestId('reply-comment-item'),
      getLoadMoreButton: () => screen.findByRole('button', { name: /load more/i }),
      user: userEvent.setup(),
      curUser,
    };
  };

  it('should render the original comment and reply list', async () => {
    const { getReplyComments } = renderComponent();

    const replyComments = await getReplyComments();

    await waitFor(() => {
      expect(replyComments.length).toBeGreaterThan(1);
    });
  });

  it('should show "Load more" button when more pages exist', async () => {
    const { getLoadMoreButton } = renderComponent();

    expect(await getLoadMoreButton()).toBeInTheDocument();
  });

  it('should load more reply comments when "Load More" is clicked', async () => {
    const { getReplyComments, user, getLoadMoreButton } = renderComponent();

    await user.click(await getLoadMoreButton());

    const replyComments = await getReplyComments();

    await waitFor(() => {
      expect(replyComments.length).toEqual(4);
    });
  });
});
