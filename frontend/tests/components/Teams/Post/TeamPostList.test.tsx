import { screen, render } from '@testing-library/react';
import userEvent from '@testing-library/user-event';

import { connectWebSocket, disconnectWebSocket } from '../../../../src/util/WebSocketService';
import { getLoggedInUser } from '../../../utils';
import { createTeamPosts } from '../../../mocks/dbActions';
import TeamPostList from '../../../../src/components/Teams/Post/TeamPostList';

describe('TeamPostList', () => {
  const getWrapper = () => {
    const { curUser, wrapper } = getLoggedInUser(
      {},
      {
        team: {
          teamPosts: createTeamPosts(4),
          teamPostPagination: {
            page: -1,
            pageSize: 2,
            totalPages: 2,
            direction: 'next',
            totalElements: 4,
          },
        },
      }
    );

    return { curUser, wrapper };
  };

  const renderComponent = () => {
    const { curUser, wrapper } = getWrapper();

    render(<TeamPostList />, { wrapper });

    return {
      curUser,
      user: userEvent.setup(),
      getButton: () => screen.getByRole('button', { name: /load more/i }),
    };
  };

  it('should render the heading and description', () => {
    renderComponent();

    const heading = screen.getByRole('heading', { level: 3, name: /team posts/i });
    const subHeading = screen.getByText(/code snippets/i);

    expect(heading).toBeInTheDocument();
    expect(subHeading).toBeInTheDocument();
  });

  it('should render team posts', async () => {
    renderComponent();

    const teamPosts = await screen.findAllByTestId('team-post-item');

    expect(teamPosts.length).toBe(4);
  });

  it('should show "Load more..." and fetches more posts', async () => {
    const { user, getButton } = renderComponent();

    await user.click(getButton());

    const teamPosts = await screen.findAllByTestId('team-post-item');

    expect(teamPosts.length).toBe(6);
  });

  it('should call WebSocket connect and disconnect on mount/unmount', () => {
    const { wrapper } = getWrapper();

    const { unmount } = render(<TeamPostList />, { wrapper });

    expect(connectWebSocket).toHaveBeenCalled();
    unmount();
    expect(disconnectWebSocket).toHaveBeenCalled();
  });
});
