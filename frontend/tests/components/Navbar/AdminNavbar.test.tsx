import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';
import userEvent from '@testing-library/user-event';

import AdminNavbar from '../../../src/components/Navbar/AdminNavbar';
import { getWrapper } from '../../RenderWithProviders';
import { db } from '../../mocks/db';
import { IUser } from '../../../src/interfaces';
import { Role } from '../../../src/enums';
import { mockNavigate } from '../../setup';

describe('AdminNavbar', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const renderComponent = (isMobileOpen: boolean = false) => {
    const userEntity = toPlainObject(db.user.create());

    const user: IUser = { ...userEntity, role: Role.ADMIN };

    const wrapper = getWrapper({
      navbar: { isMobileOpen },
      user: { user, token: 'token', refreshToken: 'refreshToken' },
    } as any);

    render(<AdminNavbar />, { wrapper });

    return {
      user: userEvent.setup(),
      curUser: user,
    };
  };

  it('should render logo and avatar', async () => {
    const { curUser } = renderComponent();

    expect(screen.getByRole('heading', { level: 1, name: /overwatch/i })).toBeInTheDocument();
    expect(screen.getByRole('img')).toHaveAttribute('src', curUser.avatarUrl);
  });

  it('should open and close mobile menu', async () => {
    const { user, curUser } = renderComponent();

    await user.click(screen.getByRole('img'));

    expect(await screen.findByText(curUser.fullName)).toBeInTheDocument();
    expect(await screen.findByRole('link', { name: /dashboard/i })).toBeInTheDocument();
    expect(await screen.findByRole('button', { name: /logout/i })).toBeInTheDocument();
  });

  it('should navigate to dashboard on link click', async () => {
    const { user, curUser } = renderComponent();

    await user.click(screen.getByRole('img'));
    const dashboardLink = await screen.findByRole('link', { name: /dashboard/i });

    expect(dashboardLink).toHaveAttribute('href', `/dashboard/${curUser.slug}`);
  });

  it('should logout the user correctly', async () => {
    const { user } = renderComponent();

    await user.click(screen.getByRole('img'));
    const logoutButton = await screen.findByRole('button', { name: /logout/i });

    await user.click(logoutButton);

    await waitFor(() => {
      expect(mockNavigate).toHaveBeenCalledWith('/signin');
    });
  });
});
