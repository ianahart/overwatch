import { screen, render } from '@testing-library/react';
import { toPlainObject } from 'lodash';

import { ITeam } from '../../../../../src/interfaces';
import { db } from '../../../../mocks/db';
import { getLoggedInUser } from '../../../../utils';
import userEvent from '@testing-library/user-event';
import MessageList from '../../../../../src/components/Teams/TeamMember/TeamPinnedMessage/MessageList';

describe('MessageList', () => {
  const getProps = () => {
    const team: ITeam = toPlainObject(db.team.create());

    return {
      team,
    };
  };

  const renderComponent = () => {
    const { wrapper } = getLoggedInUser();

    const props = getProps();

    render(<MessageList {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      props,
    };
  };

  it('should render pinned messages after loading', async () => {
    renderComponent();
    const messages = await screen.findAllByTestId('message-list-item');

    expect(messages.length).toBe(2);
  });
});
