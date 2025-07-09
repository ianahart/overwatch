import { screen, render } from '@testing-library/react';

import UserSidebarNavigation from '../../../src/components/Dashboard/UserSidebarNavigation';
import { getLoggedInUser } from '../../utils';

describe('UserSidebarNavigation', () => {
  const renderComponent = () => {
    const { curUser, wrapper } = getLoggedInUser();

    render(<UserSidebarNavigation />, { wrapper });

    return {
      curUser,
    };
  };

  it('should render the dashboard title', () => {
    renderComponent();
    expect(screen.getByRole('heading', { name: /overwatch/i })).toBeInTheDocument();
  });

  it('renders all user sidebar navigation links', () => {
    const { curUser } = renderComponent();

    const navLinks = screen.getAllByRole('link');

    expect(navLinks.length).toBe(5);

    const paths = navLinks.map((link) => link.getAttribute('data-path'));
    const labels = navLinks.map((link) => link.textContent);

    expect(paths).toEqual([
      'user/add-review',
      'user/reviews',
      `/settings/${curUser.slug}/connects`,
      'user/testimonials',
      'user/suggestions/create',
    ]);

    expect(labels).toEqual(['Get a review', 'Your Reviews', 'Your Connects', 'Testimonials', 'Add Suggestions']);
  });
});
