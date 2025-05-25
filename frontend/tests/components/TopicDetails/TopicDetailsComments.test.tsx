import { screen, render, waitFor } from '@testing-library/react';

import TopicDetailsComments from '../../../src/components/TopicDetails/TopicDetailsComments';
import { AllProviders } from '../../AllProviders';
import userEvent from '@testing-library/user-event';

describe('TopicDetailsComments', () => {
  const getProps = (overrides = {}) => {
    return {
      topicId: 1,
      ...overrides,
    };
  };

  const renderComponent = () => {
    const props = getProps();

    render(<TopicDetailsComments {...props} />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      getNextButton: () => screen.findByRole('button', { name: /next/i }),
      getPrevButton: () => screen.findByRole('button', { name: /prev/i }),
      props,
    };
  };

  it.todo('should allow sorting comments via dropdown', () => {});

  it('should render comments from the API', async () => {
    renderComponent();

    await waitFor(() => {
      expect(screen.getByText(/loading comments/i)).toBeInTheDocument();
    });

    const replyComments = await screen.findAllByTestId('topic-details-comment-item');
    expect(replyComments.length).toBeGreaterThan(1);
  });

  it('should handle pagination (next)', async () => {
    const { user, getNextButton, getPrevButton } = renderComponent();

    await user.click(await getNextButton());

    expect(await getPrevButton()).toBeInTheDocument();
    expect(await screen.findByText('2 of 10')).toBeInTheDocument();
  });

  it('should handle pagination (prev)', async () => {
    const { user, getNextButton, getPrevButton } = renderComponent();

    await user.click(await getNextButton());
    await user.click(await getPrevButton());

    await waitFor(() => {
      expect(screen.queryByRole('button', { name: /prev/i })).not.toBeInTheDocument();
    });
    expect(await screen.findByText('1 of 10')).toBeInTheDocument();
  });
});
