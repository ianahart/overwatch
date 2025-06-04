import { screen, render } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { toPlainObject } from 'lodash';

import Navbar from '../../../src/components/Navbar';
import { Role } from '../../../src/enums';
import { getLoggedInUser } from '../../utils';
import { IUser } from '../../../src/interfaces';
import { db } from '../../mocks/db';

describe('Navbar', () => {
  beforeEach(() => {
    db.user.deleteMany({ where: { id: { lte: 999 } } });
  });

  const renderComponent = (role: Role, loggedIn: boolean) => {
    const { curUser, wrapper } = getLoggedInUser({ loggedIn, role });

    const user: IUser = { ...toPlainObject(curUser) };

    render(<Navbar />, { wrapper });

    return {
      getHamburgerMenu: () => screen.getByTestId('auth-hamburger-menu'),
      user,
      getAvatar: () => screen.getByRole('img'),
      event: userEvent.setup(),
    };
  };

  it('should render the AuthNavbar component', async () => {
    const { event, user, getAvatar } = renderComponent(Role.USER, true);

    expect(getAvatar()).toHaveAttribute('src', user.avatarUrl);
    expect(getAvatar()).toHaveAttribute('alt', user.abbreviation);

    await event.click(getAvatar());

    expect(await screen.findByRole('button', { name: /logout/i })).toBeInTheDocument();
  });

  it('should render the GuestNavbar', async () => {
    renderComponent(Role.UNASSIGNED, false);
    const guestLinks = ['About', 'Community', 'Sign Up', 'Sign In'];

    guestLinks.forEach((link) => {
      expect(screen.getByRole('link', { name: link })).toBeInTheDocument();
    });
  });

  it('should render the AdminNavbar', async () => {
    const { event, getAvatar } = renderComponent(Role.ADMIN, true);

    await event.click(getAvatar());

    expect(await screen.findByRole('link', { name: /dashboard/i })).toBeInTheDocument();
    expect(await screen.findByRole('button', { name: /logout/i })).toBeInTheDocument();
  });
});
