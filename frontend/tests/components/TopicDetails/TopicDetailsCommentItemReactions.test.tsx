import { screen, render, waitFor } from '@testing-library/react';

import TopicDetailsCommentItemReactions from '../../../src/components/TopicDetails/TopicDetailsCommentItemReactions';
import { AllProviders } from '../../AllProviders';
import { createReactions } from '../../mocks/dbActions';
import userEvent from '@testing-library/user-event';

describe('TopicDetailsCommentItemReactions', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getReactions = (amount: number) => {
    return createReactions(amount);
  };

  const renderComponent = () => {
    const reactions = getReactions(2);
    render(<TopicDetailsCommentItemReactions reactions={reactions} />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      reactions,
    };
  };
  it('should render eomji reactions in collapsed state', async () => {
    const { reactions } = renderComponent();

    for (let i = 0; i < reactions.length; i++) {
      expect(screen.getByText(reactions[i].emoji)).toBeInTheDocument();
      expect(screen.queryByText(reactions[i].count)).not.toBeInTheDocument();
    }
  });

  it('should render full reaction view on click', async () => {
    const { user, reactions } = renderComponent();

    await user.click(screen.getByText(reactions[0].emoji));

    expect(screen.getByText(reactions[0].count)).toBeInTheDocument();
  });

  it('should close the popover when click-away is triggered', async () => {
    const reactions = getReactions(2);
    render(
      <div>
        <TopicDetailsCommentItemReactions reactions={reactions} />
        <div data-testid="outside">Outside Element</div>
      </div>
    );

    const user = userEvent.setup();

    await user.click(screen.getByText(reactions[0].emoji));

    await user.click(screen.getByTestId('outside'));

    await waitFor(() => {
      expect(screen.queryByText(reactions[0].count)).not.toBeInTheDocument();
    });
  });
});
