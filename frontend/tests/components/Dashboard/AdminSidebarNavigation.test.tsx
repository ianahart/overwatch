import { screen, render } from '@testing-library/react';
import AdminSidebarNavigation from '../../../src/components/Dashboard/AdminSidebarNavigation';
import { AllProviders } from '../../AllProviders';

describe('AdminSidebarNavigation', () => {
  const getAdminDashboardLinks = () => {
    return {
      transaction: ['Refunds', 'Transactions'],
      user: ['Banned Users', 'All Users'],
      content: ['Flagged Comments', 'Testimonials', 'Suggestions'],
      badge: ['Create a Badge', 'Badges'],
      security: ['MF Authentication'],
    };
  };

  const assertLinksArePresent = (links: string[]) => {
    links.forEach((link) => {
      expect(screen.getByRole('link', { name: link })).toBeInTheDocument();
    });
  };

  const renderComponent = () => {
    render(<AdminSidebarNavigation />, { wrapper: AllProviders });
  };

  it('should render the Dashboard title and Admin label', () => {
    renderComponent();

    expect(screen.getByRole('heading', { name: /overwatch/i, level: 2 })).toBeInTheDocument();
    expect(screen.getByText('Admin')).toBeInTheDocument();
  });

  it('should render all navigation block section titles', () => {
    renderComponent();

    expect(screen.getByRole('heading', { name: /transaction/i, level: 3 })).toBeInTheDocument();
    expect(screen.getByRole('heading', { name: /user/i, level: 3 })).toBeInTheDocument();
    expect(screen.getByRole('heading', { name: /content/i, level: 3 })).toBeInTheDocument();
    expect(screen.getByRole('heading', { name: /badges/i, level: 3 })).toBeInTheDocument();
    expect(screen.getByRole('heading', { name: /security/i, level: 3 })).toBeInTheDocument();
  });

  it('should render all transation links', () => {
    renderComponent();

    const { transaction } = getAdminDashboardLinks();

    assertLinksArePresent(transaction);
  });

  it('should render all user links', () => {
    renderComponent();

    const { user } = getAdminDashboardLinks();

    assertLinksArePresent(user);
  });

  it('should render all content links', () => {
    renderComponent();

    const { content } = getAdminDashboardLinks();

    assertLinksArePresent(content);
  });

  it('should render all badge links', () => {
    renderComponent();

    const { badge } = getAdminDashboardLinks();

    assertLinksArePresent(badge);
  });

  it('should render all user links', () => {
    renderComponent();

    const { security } = getAdminDashboardLinks();

    assertLinksArePresent(security);
  });
});
