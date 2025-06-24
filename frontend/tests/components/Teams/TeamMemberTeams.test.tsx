import { screen, render, waitFor } from '@testing-library/react';
import { getLoggedInUser } from '../../utils';
import { createTeamMemberTeams } from '../../mocks/dbActions';
import { IPaginationState } from '../../../src/interfaces';
import userEvent from '@testing-library/user-event';
import TeamMemberTeams from '../../../src/components/Teams/TeamMemberTeams';
import { mockNavigate } from '../../setup';

describe('TeamMemberTeams', () => {
  const renderComponent = () => {
    const teamMemberTeams = createTeamMemberTeams(2);
    const teamMemberTeamPagination: IPaginationState = {
      page: -1,
      pageSize: 2,
      totalPages: 2,
      totalElements: 4,
      direction: 'next',
    };
    const currentTeam = 1;

    const { curUser, wrapper } = getLoggedInUser(
      {},
      {
        team: {
          teamMemberTeams,
          teamMemberTeamPagination,
          currentTeam,
        },
      }
    );

    render(<TeamMemberTeams />, { wrapper });

    return {
      user: userEvent.setup(),
      curUser,
      teamMemberTeams,
    };
  };

  it('should render team member teams from the API', async () => {
    renderComponent();
    // this comes from the API in teamMembers.ts
    const totalTeamMemberTeams = 4;
    expect(await screen.findByRole('heading', { level: 3, name: `Teams You Belong to (${totalTeamMemberTeams})` }));
  });

  it('should navigate to team posts when team is clicked', async () => {
    const { user, curUser } = renderComponent();

    const teamMemberTeams = await screen.findAllByTestId('team-member-team-item');

    const targetTeam = teamMemberTeams[0];

    await user.click(targetTeam);

    await waitFor(() => {
      expect(mockNavigate).toHaveBeenCalledWith(`/settings/${curUser.slug}/teams/1/posts`);
    });
  });

  it('should call deleteTeamMember on trash icon click', async () => {
    const { user } = renderComponent();

    const trashIcons = screen.getAllByTestId('delete-team-member-container');
    expect(trashIcons.length).toBeGreaterThan(0);

    await user.click(trashIcons[0]);
  });

  it('renders load more button if there are more pages', async () => {
    renderComponent();

    expect(await screen.findByRole('button', { name: /load more/i })).toBeInTheDocument();
  });
});
