import { screen, render } from '@testing-library/react';

import { getLoggedInUser } from '../../../utils';
import Notification from '../../../../src/components/Settings/Notification';

describe('Notification', () => {
  const renderComponent = () => {
    const setting: any = {
      reviewInProgressNotifOn: true,
      reviewInCompleteNotifOn: true,
      reviewCompletedNotifOn: false,
      paymentAcknowledgementNotifOn: true,
      requestPendingNotifOn: false,
      requestAcceptedNotifOn: true,
      commentReplyOn: true,
      emailOn: false,
    };

    const { wrapper } = getLoggedInUser(
      {},
      {
        setting: {
          setting,
        },
      }
    );

    render(<Notification />, { wrapper });
  };

  it('should render all notification switches', () => {
    renderComponent();

    const switches = screen.getAllByTestId('notification-switch-item');

    expect(switches.length).toBe(8);
  });
});
