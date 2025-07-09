import { screen, render } from '@testing-library/react';
import { getLoggedInUser } from '../../utils';
import AdminDashboard from '../../../src/components/Dashboard/AdminDashboard';
import { mockNavigate } from '../../setup';

describe('AdminDashboard', () => {
  const renderComponent = (overrides = {}) => {
    const { curUser, wrapper } = getLoggedInUser({ ...overrides });

    render(<AdminDashboard />, { wrapper });

    return {
      curUser,
    };
  };

  it('should render the component and <AdminSidebarNavigation />', () => {
    renderComponent({ role: 'ADMIN' });

    expect(screen.getByTestId('AdminDashboard')).toBeInTheDocument();
    expect(screen.getByTestId('AdminSidebarNavigation')).toBeInTheDocument();
  });

  it('should let admin navigate to the dashboard with role as "ADMIN"', () => {
    const { curUser } = renderComponent({ role: 'ADMIN' });

    expect(mockNavigate).toHaveBeenCalledWith(`/admin/dashboard/${curUser.slug}`);
  });

  it('should navigate to the homepage if the user is NOT an "ADMIN"', () => {
    renderComponent({ role: 'USER' });

    expect(mockNavigate).toHaveBeenCalledWith('/');
  });
});
