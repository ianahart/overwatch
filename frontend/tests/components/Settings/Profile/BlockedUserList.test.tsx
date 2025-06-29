import { screen, render } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { HttpResponse, http } from 'msw';

import BlockedUserList from '../../../../src/components/Settings/Profile/BlockedUserList';
import { getLoggedInUser } from '../../../utils';
import { server } from '../../../mocks/server';
import { baseURL } from '../../../../src/util';

describe('BlockedUserList', () => {
  const renderComponent = () => {
    const { wrapper } = getLoggedInUser();

    render(<BlockedUserList />, { wrapper });

    return {
      user: userEvent.setup(),
    };
  };

  it('should render empty state when no users returned', async () => {
    server.use(
      http.get(`${baseURL}/block-users`, ({ request }) => {
        const url = new URL(request.url);
        console.log(url);
        return HttpResponse.json(
          {
            data: {
              items: [],
              page: 0,
              pageSize: 3,
              totalPages: 0,
              direction: 'next',
              totalElements: 0,
            },
          },
          { status: 200 }
        );
      })
    );

    renderComponent();

    expect(await screen.findByText('You currently have no blocked users.')).toBeInTheDocument();
  });

  it('should render blocked users list when data is returned', async () => {
    renderComponent();

    const blockedUsers = await screen.findAllByTestId('blocked-user-item');

    expect(blockedUsers.length).toBe(2);
  });

  it('should show "More users" button when there are more pages', async () => {
    renderComponent();

    expect(await screen.findByRole('button', { name: /more users/i })).toBeInTheDocument();
  });

  it('should load more users when clicking "More users" button', async () => {
    const { user } = renderComponent();

    const moreUsersBtn = await screen.findByRole('button', { name: /more users/i });

    await user.click(moreUsersBtn);

    const blockedUsers = await screen.findAllByTestId('blocked-user-item');

    expect(blockedUsers.length).toBe(4);
  });
});
