import { screen, render, waitFor } from '@testing-library/react';

import TopicDetailsCommentItemVote from '../../../src/components/TopicDetails/TopicDetailsCommentItemVote';
import { AllProviders } from '../../AllProviders';
import userEvent from '@testing-library/user-event';

describe('TopicDetailsCommentItemVote', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const renderComponent = (overrides = {}) => {
    const props = {
      curUserHasVoted: false,
      curUserVoteType: 'UPVOTE',
      voteDifference: 1,
      createVote: vi.fn(),
      ...overrides,
    };

    render(<TopicDetailsCommentItemVote {...props} />, { wrapper: AllProviders });

    return {
      props,
      user: userEvent.setup(),
      getUpVoteButton: () => screen.getByTestId('comment-upvote'),
      getDownVoteButton: () => screen.getByTestId('comment-downvote'),
    };
  };

  it('should render the vote difference', () => {
    const { props } = renderComponent();

    expect(screen.getByText(props.voteDifference)).toBeInTheDocument();
  });

  it('should call "createVote" with "UPVOTE" when up arrow is clicked', async () => {
    const { props, user, getUpVoteButton } = renderComponent();

    await user.click(getUpVoteButton());

    expect(props.createVote).toHaveBeenCalledWith('UPVOTE');
  });

  it('shoud call "createVote" with "DOWNVOTE" when down arrow is clicked', async () => {
    const { props, user, getDownVoteButton } = renderComponent({ curUserVoteType: 'DOWNVOTE' });

    await user.click(getDownVoteButton());

    expect(props.createVote).toHaveBeenCalledWith('DOWNVOTE');
  });

  it('should apply blue class to upvote if user upvoted', async () => {
    const { user, getUpVoteButton } = renderComponent({ curUserHasVoted: true });

    await user.click(getUpVoteButton());

    await waitFor(() => {
      const upvoteIcon = screen.getByTestId('upvote-icon');
      const downvoteIcon = screen.getByTestId('downvote-icon');

      expect(upvoteIcon).toHaveClass('text-blue-400');
      expect(downvoteIcon).not.toHaveClass('text-blue-400');
    });
  });

  it('should apply blue class to downvote if user downvoted', async () => {
    const { user, getDownVoteButton } = renderComponent({
      curUserHasVoted: true,
      curUserVoteType: 'DOWNVOTE',
    });

    await user.click(getDownVoteButton());

    await waitFor(() => {
      const upvoteIcon = screen.getByTestId('upvote-icon');
      const downvoteIcon = screen.getByTestId('downvote-icon');

      expect(downvoteIcon).toHaveClass('text-blue-400');
      expect(upvoteIcon).not.toHaveClass('text-blue-400');
    });
  });
});
