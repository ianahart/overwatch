import { screen, render, waitFor } from '@testing-library/react';

import TopicDetailsCommentReaction from '../../../src/components/TopicDetails/TopicDetailsCommentReaction';
import { getLoggedInUser } from '../../utils';
import userEvent from '@testing-library/user-event';
import { baseURL } from '../../../src/util';
import { server } from '../../mocks/server';
import { HttpResponse, http } from 'msw';

describe('TopicDetailsCommentReaction', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = (overrides = {}) => {
    return {
      userId: 1,
      commentId: 1,
      updateCommentReaction: vi.fn(),
      removeCommentReaction: vi.fn(),
      ...overrides,
    };
  };

  const renderComponent = () => {
    const { wrapper, curUser } = getLoggedInUser();

    const props = getProps({ userId: curUser.id });

    render(<TopicDetailsCommentReaction {...props} />, { wrapper });

    return {
      props,
      user: userEvent.setup(),
      getEmojis: () => screen.findAllByTestId(/^emoji-/),
    };
  };

  it('should render all emoji buttons', async () => {
    const { getEmojis } = renderComponent();

    const emojis = await getEmojis();

    expect(emojis.length).toBeGreaterThan(0);
  });
  it('should create a reaction on click when no emoji is selected', async () => {
    server.use(
      http.get(`${baseURL}/comments/1/reactions`, () => {
        return HttpResponse.json({ data: null }, { status: 200 });
      })
    );

    const { user, props, getEmojis } = renderComponent();

    await waitFor(async () => {
      const emojis = await getEmojis();
      expect(emojis).toHaveLength(6);
    });

    const emojis = await getEmojis();

    await user.click(emojis[0]);

    await waitFor(() => {
      expect(props.updateCommentReaction).toHaveBeenCalledWith('👍', props.commentId);
    });

    await waitFor(async () => {
      const updatedEmojis = await getEmojis();
      expect(updatedEmojis[0]).toHaveClass('mx-1 cursor-pointer bg-blue-400 rounded');
    });
  });

  it('should delete a reaction if already selected', async () => {
    const { user, props, getEmojis } = renderComponent();
    await waitFor(async () => {
      const emojis = await getEmojis();
      expect(emojis).toHaveLength(6);
    });

    const emojis = await getEmojis();

    await user.click(emojis[0]);
    await waitFor(() => {
      expect(props.removeCommentReaction).toHaveBeenCalledWith('👍', 1);
    });

    await waitFor(async () => {
      const updatedEmojis = await getEmojis();
      expect(updatedEmojis[0]).not.toHaveClass('bg-blue-400');
    });
  });
});
