import { screen, render, waitFor } from '@testing-library/react';

import GuestNavbar from '../../../src/components/Navbar/GuestNavbar';
import userEvent from '@testing-library/user-event';
import { getWrapper } from '../../RenderWithProviders';

describe('GuestNavbar', () => {
  const renderComponent = (isMobileOpen: boolean = false) => {
    const wrapper = getWrapper({
      navbar: { isMobileOpen },
    } as any);

    render(<GuestNavbar />, { wrapper });

    return {
      user: userEvent.setup(),
    };
  };

  it('should render logo', () => {
    renderComponent();

    expect(screen.getByRole('heading', { level: 1, name: /overwatch/i })).toBeInTheDocument();
  });

  it('should show hamburger menu and open/close', async () => {
    const { user } = renderComponent();

    const hamburgerMenu = screen.getByTestId('guest-hamburger-menu');

    expect(hamburgerMenu).toBeInTheDocument();

    await user.click(hamburgerMenu);

    const closeBtn = screen.getByTestId('guest-close-menu');

    expect(closeBtn).toBeInTheDocument();

    await user.click(closeBtn);
    await waitFor(() => {
      expect(screen.queryByTestId('guest-close-menu')?.closest('div')).toHaveClass('hidden');
    });
  });
  it('should render all navigation links with correct hrefs', async () => {
    const { user } = renderComponent();

    const hamburgerMenu = screen.getByTestId('guest-hamburger-menu');

    expect(hamburgerMenu).toBeInTheDocument();

    await user.click(hamburgerMenu);

    expect(screen.getByRole('link', { name: /about/i })).toHaveAttribute('href', '/about');
    expect(screen.getByRole('link', { name: /community/i })).toHaveAttribute('href', '/community');
    expect(screen.getByRole('link', { name: /sign in/i })).toHaveAttribute('href', '/signin');
    expect(screen.getByRole('link', { name: /sign up/i })).toHaveAttribute('href', '/signup');
  });
});
