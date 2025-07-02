import { screen, render } from '@testing-library/react';
import { toPlainObject } from 'lodash';
import userEvent from '@testing-library/user-event';

import * as WebSocketService from '../../../../src/util/WebSocketService';
import Chat from '../../../../src/components/Settings/Connects/Chat';
import { getLoggedInUser } from '../../../utils';
import { db } from '../../../mocks/db';
import { IConnection } from '../../../../src/interfaces';

describe('Chat', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const renderComponent = () => {
    const currentConnection: IConnection = toPlainObject(db.connection.create());

    const { curUser, wrapper } = getLoggedInUser(
      {},
      {
        chat: {
          messages: [],
          currentConnection,
        },
      }
    );

    render(<Chat />, { wrapper });

    return {
      curUser,
      user: userEvent.setup(),
      currentConnection,
    };
  };

  it('should render the current connection name', () => {
    const { currentConnection } = renderComponent();

    expect(screen.getByText(`${currentConnection.firstName} ${currentConnection.lastName}`)).toBeInTheDocument();
  });

  it('should fetch and display chat messages on mount', async () => {
    renderComponent();

    const messages = await screen.findAllByTestId('settings-chat-message-item');

    expect(messages.length).toBe(2);
  });

  it('should let user type and send a message', async () => {
    const sendMessageMock = vi.spyOn(WebSocketService, 'sendMessage');

    const { user } = renderComponent();
    const input = screen.getByRole('textbox');
    const button = screen.getByRole('button', { name: /send/i });

    await user.type(input, 'Test message');
    await user.click(button);

    const [[_, rawPayload]] = sendMessageMock.mock.calls;
    const parsed = JSON.parse(rawPayload);

    expect(parsed).toEqual(
      expect.objectContaining({
        connectionId: expect.any(Number),
        userId: 1,
        text: 'Test message',
      })
    );

    expect(input).toHaveValue('');
  });

  it('should let user type and send a message', async () => {
    const sendMessageMock = vi.spyOn(WebSocketService, 'sendMessage');

    const { user } = renderComponent();
    const button = screen.getByRole('button', { name: /send/i });

    await user.click(button);

    expect(sendMessageMock).not.toHaveBeenCalled();
  });
});
