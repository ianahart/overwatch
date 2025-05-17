import { screen, render } from '@testing-library/react';
import dayjs from 'dayjs';
import userEvent from '@testing-library/user-event';

import NotificationList from '../../../src/components/Notification/NotificationList';
import { AllProviders } from '../../AllProviders';
import { db } from '../../mocks/db';
import { toPlainObject } from '../../utils';
import { INotification } from '../../../src/interfaces';
import { deleteManyUser } from '../../mocks/dbActions';
import { NotificationRole, NotificationType } from '../../../src/enums';

const mockDate = dayjs().subtract(1, 'hour').toDate().toString();

const getNotifications = () => {
  const notifications: INotification[] = [];
  const receiver = db.user.create({ id: 1 });
  const sender = db.user.create({ id: 2 });

  for (let i = 0; i < 2; i++) {
    const notification = toPlainObject(
      db.notification.create({
        senderId: sender,
        receiverId: receiver,
      })
    );
    notifications.push({
      ...notification,
      senderId: notification.senderId!.id,
      createdAt: mockDate,
      receiverId: notification.receiverId!.id,
    });
  }
  notifications[0].notificationType = NotificationType.CONNECTION_REQUEST_PENDING;
  notifications[0].notificationRole = NotificationRole.RECEIVER;
  notifications[1].avatarUrl = '';

  return notifications;
};

const handleDeleteNotification = vi.fn();
const emitNotification = vi.fn();

describe('NotificationList', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    deleteManyUser([1, 2]);
  });

  const renderComponent = () => {
    const props = {
      notifications: getNotifications(),
      handleDeleteNotification,
      emitNotification,
    };
    render(<NotificationList {...props} />, { wrapper: AllProviders });
    return {
      user: userEvent.setup(),
      props,
    };
  };
  it('should render all notifications correctly', () => {
    const { props } = renderComponent();
    expect(screen.getByText(props.notifications[0].text)).toBeInTheDocument();
    expect(screen.getByText(props.notifications[1].text)).toBeInTheDocument();
  });

  it('should render avatar images correctly', () => {
    const { props } = renderComponent();

    expect(screen.getAllByRole('img')[0]).toHaveAttribute('src', props.notifications[0].avatarUrl);
  });

  it('should render the "View here" link when a notification has a link', async () => {
    const { props } = renderComponent();

    const [link] = screen.getAllByTestId(/notification-link-/i);

    expect(link).toHaveAttribute('href', props.notifications[0].link);
  });

  it('should call handleDeleteNotification when the close icon is clicked', async () => {
    const { user, props } = renderComponent();

    const closeIcons = screen.getAllByTestId(/delete-notification-/i);

    await user.click(closeIcons[0]);

    expect(props.handleDeleteNotification).toHaveBeenCalledWith(props.notifications[0]);
  });

  it('should call emitNotification when the "Accept button is clicked"', async () => {
    const { props, user } = renderComponent();

    const acceptButton = screen.getByText('Accept');

    await user.click(acceptButton);

    expect(props.emitNotification).toHaveBeenCalledWith(props.notifications[0]);
  });

  it('should call handleDeleteNotification when the "Deny" button is clicked', async () => {
    const { user, props } = renderComponent();

    const denyButton = screen.getByText('Deny');

    await user.click(denyButton);
    expect(props.handleDeleteNotification).toHaveBeenCalledWith(props.notifications[0]);
  });

  it('should display the relative time for notifications', () => {
    renderComponent();

    const relativeTimes = screen.getAllByText(/an hour ago/i);
    expect(relativeTimes[0]).toBeInTheDocument();
    expect(relativeTimes[1]).toBeInTheDocument();
  });

  it('should render notifications without avatar correctly', () => {
    const { props } = renderComponent();

    const lastNotification = screen.getByText(props.notifications[1].text);

    expect(lastNotification).toBeInTheDocument();
  });
});
