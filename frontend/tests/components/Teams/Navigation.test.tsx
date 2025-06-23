import { screen, render } from '@testing-library/react';

import Navigation from '../../../src/components/Teams/Navigation';
import { getLoggedInUser } from '../../utils';

describe('Navigation', () => {
  const renderComponent = (currentTeam = 1) => {
    const { curUser, wrapper } = getLoggedInUser(
      {},
      {
        team: {
          currentTeam,
        },
      }
    );

    render(<Navigation />, { wrapper });
    return {
      curUser,
    };
  };

  it('should render non-team links when current Team is 0', () => {
    const { curUser } = renderComponent(0);

    const invitationLink = screen.getByRole('link', { name: /view invitations/i });

    expect(invitationLink).toBeInTheDocument();
    expect(invitationLink).toHaveAttribute('href', `/settings/${curUser.slug}/teams/invitations`);
  });

  it('should render team links when current Team is not 0', () => {
    const { curUser } = renderComponent();
    const expectedLinks = [
      { text: 'View Invitations', href: `/settings/${curUser.slug}/teams/invitations` },
      { text: 'Add Team Member', href: `/settings/${curUser.slug}/teams/1/add` },
      { text: 'Messages', href: `/settings/${curUser.slug}/teams/1/messages` },
      { text: 'Posts', href: `/settings/${curUser.slug}/teams/1/posts` },
      { text: 'Team Members', href: `/settings/${curUser.slug}/teams/1/members` },
    ];

    expectedLinks.forEach(({ text, href }) => {
      const link = screen.getByRole('link', { name: text });
      expect(link).toBeInTheDocument();
      expect(link).toHaveAttribute('href', href);
    });

    const links = screen.getAllByRole('link');
    expect(links).toHaveLength(expectedLinks.length);
  });
});
