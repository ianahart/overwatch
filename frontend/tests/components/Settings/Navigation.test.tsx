import { screen, render } from '@testing-library/react';

import Navigation from '../../../src/components/Settings/Navigation';
import { getLoggedInUser } from '../../utils';
import { IUser } from '../../../src/interfaces';
import { mockLocation } from '../../setup';

describe('Navigation', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    mockLocation();
  });

  const renderComponent = (overrides: Partial<IUser> = {}) => {
    const { curUser, wrapper } = getLoggedInUser(overrides);

    render(<Navigation />, { wrapper });

    return {
      curUser,
    };
  };

  it('should render headings correctly', () => {
    renderComponent({ role: 'REVIEWER' });

    expect(screen.getByRole('heading', { name: /settings/i, level: 2 })).toBeInTheDocument();
    expect(screen.getByRole('heading', { name: /billing/i, level: 3 })).toBeInTheDocument();
    expect(screen.getByRole('heading', { name: /user settings/i, level: 3 })).toBeInTheDocument();
  });

  it('should render all REVIEWER links + billing', () => {
    renderComponent({ role: 'REVIEWER' });

    const links = screen.getAllByRole('listitem');

    expect(links.length).toBe(11);
    expect(screen.getByText('Billing & Payments')).toBeInTheDocument();
    expect(screen.getByText('Connects')).toBeInTheDocument();
    expect(screen.getByText('Contact Info')).toBeInTheDocument();
    expect(screen.getByText('My Profile')).toBeInTheDocument();
    expect(screen.getByText('Edit Profile')).toBeInTheDocument();
    expect(screen.getByText('Profile Settings')).toBeInTheDocument();
    expect(screen.getByText('Testimonials')).toBeInTheDocument();
    expect(screen.getByText('Get Paid')).toBeInTheDocument();
    expect(screen.getByText('My Teams')).toBeInTheDocument();
    expect(screen.getByText('Password & Security')).toBeInTheDocument();
    expect(screen.getByText('Notification Settings')).toBeInTheDocument();
  });

  it('should render all REVIEWER links + billing', () => {
    renderComponent({ role: 'USER' });

    const links = screen.getAllByRole('listitem');

    expect(links.length).toBe(8);
    expect(screen.getByText('Billing & Payments')).toBeInTheDocument();
    expect(screen.getByText('Connects')).toBeInTheDocument();
    expect(screen.getByText('Contact Info')).toBeInTheDocument();
    expect(screen.getByText('My Profile')).toBeInTheDocument();
    expect(screen.getByText('Edit Profile')).toBeInTheDocument();
    expect(screen.getByText('Profile Settings')).toBeInTheDocument();
    expect(screen.getByText('Password & Security')).toBeInTheDocument();
    expect(screen.getByText('Notification Settings')).toBeInTheDocument();
  });
});
