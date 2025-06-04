import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';

import AuthNavbar from '../../../src/components/Navbar/AuthNavbar';
import { db } from '../../mocks/db';
import { IUser } from '../../../src/interfaces';
import { getWrapper } from '../../RenderWithProviders';
import { Role } from '../../../src/enums';
import userEvent from '@testing-library/user-event';
import { mockNavigate } from '../../setup';

describe('AuthNavbar', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const renderComponent = (isMobileOpen: boolean = false) => {
    const userEntity = toPlainObject(db.user.create());

    const user: IUser = { ...userEntity, role: Role.REVIEWER };

    const wrapper = getWrapper({
      navbar: { isMobileOpen },
      user: { user, token: 'token', refreshToken: 'refreshToken' },
    } as any);

    render(<AuthNavbar />, { wrapper });

    return {
      user: userEvent.setup(),
      getHeading: () => screen.getByRole('heading', { level: 1, name: /overwatch/i }),
      getAvatar: () => screen.getByRole('img'),
      curUser: user,
    };
  };

  it('should render avatar and logo', () => {
    const { getHeading, getAvatar, curUser } = renderComponent();

    expect(getHeading()).toBeInTheDocument();
    expect(getAvatar()).toHaveAttribute('src', curUser.avatarUrl);
  });

  it('should open and close menu', async () => {
    const { getAvatar, user } = renderComponent();

    await user.click(getAvatar());

    const closeIcon = await screen.findByTestId('auth-navbar-close-icon');

    expect(closeIcon).toBeInTheDocument();

    await user.click(closeIcon);

    await waitFor(() => {
      expect(screen.queryByRole('button', { name: /logout/i })).not.toBeInTheDocument();
    });
  });

  it('should contain all links with correct href', async () => {
    const { getAvatar, user, curUser } = renderComponent();

    await user.click(getAvatar());

    expect(await screen.findByRole('link', { name: /dashboard/i })).toHaveAttribute(
      'href',
      `/dashboard/${curUser.slug}`
    );
    expect(await screen.findByRole('link', { name: /explore/i })).toHaveAttribute('href', '/explore/most-recent');
    expect(await screen.findByRole('link', { name: /community/i })).toHaveAttribute('href', '/community');
    expect(await screen.findByRole('link', { name: /settings/i })).toHaveAttribute(
      'href',
      `/settings/${curUser.slug}/profile`
    );
  });

  it('should logout the user correctly', async () => {
    const { user, getAvatar } = renderComponent();
    await user.click(getAvatar());

    await user.click(await screen.findByRole('button', { name: /logout/i }));

    await waitFor(() => {
      expect(mockNavigate).toHaveBeenCalledWith('/signin');
    });
  });
});
