import { screen, render } from '@testing-library/react';

import ReviewerSidebarNavigation from '../../../src/components/Dashboard/ReviewerSidebarNavigation';
import { getLoggedInUser } from '../../utils';

describe('ReviewerSidebarNavigation', () => {
  const renderComponent = () => {
    const { curUser, wrapper } = getLoggedInUser();

    render(<ReviewerSidebarNavigation />, { wrapper });

    return {
      curUser,
    };
  };

  it('should render dashboard title', () => {
    renderComponent();

    expect(screen.getByRole('heading', { name: /overwatch/i })).toBeInTheDocument();
  });

  it('should render all reviewer navigation links', () => {
    const { curUser } = renderComponent();

    const links = screen.getAllByRole('link');

    expect(links.length).toBe(6);

    const paths = links.map((link) => link.getAttribute('data-path'));
    expect(paths).toContain('reviewer/reviews');
    expect(paths).toContain(`/settings/${curUser.slug}/connects`);
    expect(paths).toContain('reviewer/stats');
    expect(paths).toContain('reviewer/workspaces');
    expect(paths).toContain('reviewer/suggestions/create');
    expect(paths).toContain('reviewer/badges');

    const labels = links.map((link) => link.textContent);
    expect(labels).toEqual([
      'Your Reviews',
      'Your Connects',
      'Your Statistics',
      'Your Workspaces',
      'Add Suggestions',
      'Badges',
    ]);
  });
});
