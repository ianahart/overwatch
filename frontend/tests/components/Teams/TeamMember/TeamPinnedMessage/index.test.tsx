import { screen, render } from '@testing-library/react';
import { toPlainObject } from 'lodash';

import TeamPinnedMessage from '../../../../../src/components/Teams/TeamMember/TeamPinnedMessage';
import { ITeam, IUser } from '../../../../../src/interfaces';
import { getLoggedInUser } from '../../../../utils';
import { db } from '../../../../mocks/db';
import userEvent from '@testing-library/user-event';

describe('TeamPinnedMessage', () => {
  const getProps = (user: IUser, overrides = {}) => {
    const team: ITeam = { ...toPlainObject(db.team.create()), userId: user.id, ...overrides };

    return { team };
  };

  const renderComponent = () => {
    const { curUser, wrapper } = getLoggedInUser();

    const props = getProps(toPlainObject(curUser));

    render(<TeamPinnedMessage {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      props,
    };
  };

  it('should render the title and description', () => {
    renderComponent();

    expect(screen.getByRole('heading', { name: /pinned messages/i })).toBeInTheDocument();
    expect(screen.getByText(/pinned by the admin/)).toBeInTheDocument();
  });

  it('should show "New Message" button if user is team admin', () => {
    renderComponent();

    expect(screen.getByRole('button', { name: /new message/i })).toBeInTheDocument();
  });

  it('should not show "New Message" button if user is not team admin', () => {
    const { curUser, wrapper } = getLoggedInUser();
    const props = getProps(toPlainObject(curUser), { userId: 999 });

    render(<TeamPinnedMessage {...props} />, { wrapper });

    expect(screen.queryByRole('button', { name: /new message/i })).not.toBeInTheDocument();
  });
});
