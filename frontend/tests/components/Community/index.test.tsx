import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import Community from '../../../src/components/Community';
import { db } from '../../mocks/db';
import { getWrapper } from '../../RenderWithProviders';
import { faker } from '@faker-js/faker';

describe('Community', () => {
  const renderComponent = (overrides = {}) => {
    const user = {
      ...db.user.create(),
      id: 1,
      ...overrides,
      token: faker.lorem.word(),
    };

    const wrapper = getWrapper({
      user: {
        user,
        token: user.token,
      },
    } as any);
    render(<Community />, { wrapper });

    return {
      user: userEvent.setup(),
      topicList: () => screen.queryByText(/Topics/i),
      savedCommentsList: () => screen.queryByText(/Saved Comments/i),
      yourTopicsList: () => screen.queryByText(/Your Topics/i),
    };
  };

  it('should render header, search bar, and create topic link', () => {
    renderComponent();

    expect(screen.getByText(/Community/i)).toBeInTheDocument();
    expect(screen.getByText(/Explore topics being discussed/i)).toBeInTheDocument();
    expect(screen.getByText(/Create new topic/i)).toBeInTheDocument();
    expect(screen.getByText(/Search/i)).toBeInTheDocument();
  });

  it('should render the correct active tab by default', () => {
    renderComponent();
    const activeClassNames = 'font-bold text-green-400';

    expect(screen.getByRole('button', { name: 'Topics' })).toHaveClass(activeClassNames);
    expect(screen.queryByText(/Saved Comments/i)).not.toHaveClass(activeClassNames);
    expect(screen.queryByText(/Your Topics/i)).not.toHaveClass(activeClassNames);
  });

  it('should switch to "Saved Comments" tab and displays the correct component', async () => {
    const { user } = renderComponent();

    await user.click(screen.getByRole('button', { name: 'Saved Comments' }));

    expect(await screen.findByTestId('saved-comment-list')).toBeInTheDocument();
  });

  it('should switche to "Your Topics" tab and displays the correct component', async () => {
    const { user } = renderComponent();

    await user.click(screen.getByRole('button', { name: 'Your Topics' }));

    expect(await screen.findByTestId('user-topic-list')).toBeInTheDocument();
  });

  it('does not render "Saved Comments" or "Your Topics" if user is not logged in', async () => {
    renderComponent({ user: { id: 0 } });

    await userEvent.click(screen.getByText('Saved Comments'));
    expect(screen.queryByText('CommunityTopicSavedCommentList')).not.toBeInTheDocument();

    await userEvent.click(screen.getByText('Your Topics'));
    expect(screen.queryByText('CommunityUserTopicList')).not.toBeInTheDocument();
  });

  it('displays "Topics" list when "Topics" tab is clicked', async () => {
    const { user } = renderComponent();

    await user.click(screen.getByRole('button', { name: 'Topics' }));

    expect(await screen.findByTestId('plain-topic-list')).toBeInTheDocument();
  });
});
