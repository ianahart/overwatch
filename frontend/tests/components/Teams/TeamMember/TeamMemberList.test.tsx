import { screen, render } from '@testing-library/react';
import { setMockParams } from '../../../setup';
import { getLoggedInUser } from '../../../utils';
import { IPaginationState } from '../../../../src/interfaces';
import TeamMemberList from '../../../../src/components/Teams/TeamMember/TeamMemberList';
import userEvent from '@testing-library/user-event';

describe('TeamMemberList', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    setMockParams({ teamId: '1' });
  });

  const getTeamMemberState = () => {
    const teamMemberPagination: IPaginationState = {
      page: 0,
      pageSize: 2,
      totalPages: 6,
      totalElements: 12,
      direction: 'next',
    };

    return {
      teamMembers: [],
      teamMemberPagination,
    };
  };

  const getProps = () => {
    return {
      getButton: () => screen.getByRole('button', { name: /load more/i }),
    };
  };

  const renderComponent = () => {
    const { teamMembers, teamMemberPagination } = getTeamMemberState();

    const { curUser, wrapper } = getLoggedInUser(
      {},
      {
        team: {
          teamMembers,
          teamMemberPagination,
        },
      }
    );

    render(<TeamMemberList />, { wrapper });

    const props = getProps();

    return {
      props,
      user: userEvent.setup(),
      curUser,
    };
  };

  it('should render loading state and team members title', () => {
    renderComponent();
    expect(screen.getByRole('heading', { name: /team members/i })).toBeInTheDocument();
  });

  it('should render admin and team members', async () => {
    renderComponent();

    const teamMemberItems = await screen.findAllByTestId('team-member-item');
    const admin = await screen.findAllByTestId('team-member-admin');

    expect(admin.length).toBe(1);
    expect(teamMemberItems.length).toBe(2);
  });
});
