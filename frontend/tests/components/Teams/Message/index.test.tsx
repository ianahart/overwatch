import { screen, render, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { faker } from '@faker-js/faker';

import { sendMessage } from '../../../../src/util/WebSocketService';
import Message from '../../../../src/components/Teams/Message';
import { getWrapper } from '../../../RenderWithProviders';
import { db } from '../../../mocks/db';

describe('Message', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    db.teamInvitation.deleteMany({ where: { id: { lte: 999 } } });
  });

  const createCurUser = () => {
    return {
      ...db.user.create(),
      id: 1,
      token: faker.lorem.word(),
    };
  };

  const getProps = () => {
    const curUser = createCurUser();

    return {
      curUser,
    };
  };

  const renderComponent = () => {
    const props = getProps();

    const wrapper = getWrapper({
      user: {
        user: props.curUser,
        token: props.curUser.token,
      },
      team: {
        currentTeam: 1,
        teamMessages: [],
      },
    } as any);

    render(<Message />, { wrapper });

    return {
      user: userEvent.setup(),
      getTeamMessages: () => screen.findAllByTestId('team-message'),
      getInput: () => screen.getByRole('textbox'),
      getButton: () => screen.getByRole('button', { name: /send/i }),
      props,
    };
  };
  it('should fetch and display messages on mount', async () => {
    const { getTeamMessages } = renderComponent();

    const messages = await getTeamMessages();
    expect(messages.length).toBe(10);
  });

  it('should show messages on left/right depending on the userId', async () => {
    const { getTeamMessages } = renderComponent();

    const [sentBySelf, sentByOther, ..._] = await getTeamMessages();

    expect(sentByOther.closest('.flex')?.className).toContain('items-start');
    expect(sentBySelf.closest('.flex')?.className).toContain('items-end');
  });

  it('should let the user type and send a message', async () => {
    const { user, getButton, getInput } = renderComponent();

    await user.type(getInput(), 'test message');
    await user.click(getButton());

    await waitFor(() => {
      const payload = {
        teamId: 1,
        userId: 1,
        text: 'test message',
      };
      expect(sendMessage).toHaveBeenCalledWith('/api/v1/team-messages', JSON.stringify(payload));
      expect(getInput()).toHaveValue('');
    });
  });
});
