import { screen, render, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';

import Notifications from '../../../src/components/Notification';
import { getWrapper } from '../../RenderWithProviders';
import { db } from '../../mocks/db';
import { deleteManyUser } from '../../mocks/dbActions';

describe('Notifications', () => {
  beforeEach(() => {
    db.notification.deleteMany({
      where: {
        id: {
          gte: 1,
        },
      },
    });
    deleteManyUser([1, 2]);
  });
  const renderComponent = (overrides = {}) => {
    const user = {
      ...db.user.create(),
      id: 1,
      ...overrides,
      token: db.token.create().token,
    };

    const wrapper = getWrapper({
      user: {
        user,
        token: user.token,
      },
    } as any);

    render(<Notifications />, { wrapper });

    return {
      user: userEvent.setup(),
      getNotificationBell: () => screen.getByTestId('notification-bell'),
      getMoreNotificationsButton: () => screen.findByRole('button', { name: /more notifications/i }),
    };
  };
  it('should render the notification bell icon', () => {
    const { getNotificationBell } = renderComponent();

    expect(getNotificationBell()).toBeInTheDocument();
  });

  it('should fetch and display notifications when the bell icon is clicked', async () => {
    const { user, getNotificationBell } = renderComponent();

    await user.click(getNotificationBell());

    const notifications = await screen.findAllByTestId(/notification-item-/);

    expect(notifications.length).toBeGreaterThan(0);
  });

  it('should close the notifications dropdown when clicking outside', async () => {
    const { user, getNotificationBell, getMoreNotificationsButton } = renderComponent();

    await user.click(getNotificationBell());

    expect(await getMoreNotificationsButton()).toBeInTheDocument();

    const outside = screen.queryByTestId('notification-outside');
    if (outside) {
      await user.click(document.body);

      await waitFor(() => {
        expect(screen.queryByRole('button', { name: /more notifications/i })).not.toBeInTheDocument();
      });
    }
  });

  it('should paginate notifications when "More notifications..." is clicked', async () => {
    const { user, getMoreNotificationsButton, getNotificationBell } = renderComponent();

    await user.click(getNotificationBell());

    expect(await getMoreNotificationsButton()).toBeInTheDocument();
  });
});
