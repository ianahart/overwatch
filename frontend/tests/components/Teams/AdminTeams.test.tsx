import { screen, render } from '@testing-library/react';
import userEvent from '@testing-library/user-event';

import AdminTeams from '../../../src/components/Teams/AdminTeams';
import { getLoggedInUser } from '../../utils';
import { setCurrentTeam } from '../../../src/state/store';
import { mockDispatch } from '../../setup'; // import your global mockDispatch
import { createTeams } from '../../mocks/dbActions';

vi.mock('../../../src/state/store', async () => {
  const actual = await vi.importActual('../../../src/state/store');
  return {
    ...actual,
    setCurrentTeam: vi.fn(),
  };
});

describe('AdminTeams', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    mockDispatch.mockClear();
  });

  const renderComponent = () => {
    const adminTeams = createTeams(2);
    adminTeams[0].totalTeams = 4;

    const { curUser, wrapper } = getLoggedInUser(
      {},
      {
        team: {
          adminTeams,
          currentTeam: 1,
          adminTeamPagination: {
            page: -1,
            pageSize: 2,
            totalPages: 2,
            totalElements: 4,
            direction: 'next',
          },
        },
      }
    );

    render(<AdminTeams />, { wrapper });

    return {
      user: userEvent.setup(),
      curUser,
      getAdminTeams: () => screen.findAllByTestId('admin-team-item'),
    };
  };

  it('should render team list and show count', async () => {
    const { getAdminTeams } = renderComponent();

    const adminTeams = await getAdminTeams();

    expect(adminTeams.length).toBe(2);
    expect(await screen.findByRole('heading', { level: 3, name: `Teams You Manage (4)` })).toBeInTheDocument();
  });

  it('should allow clicking on a team triggering dispatch', async () => {
    const fakeAction: ReturnType<typeof setCurrentTeam> = setCurrentTeam(1);

    vi.mocked(setCurrentTeam).mockReturnValue(fakeAction);

    const { user, getAdminTeams } = renderComponent();

    const adminTeams = await getAdminTeams();

    await user.click(adminTeams[0]);

    expect(mockDispatch).toHaveBeenCalledWith(fakeAction);
  });

  it('should load more teams when clicking on "Load more..."', async () => {
    const { user, getAdminTeams } = renderComponent();

    const paginateButton = await screen.findByRole('button', { name: /load more.../i });

    expect(paginateButton).toBeInTheDocument();
    await user.click(paginateButton);

    const adminTeams = await getAdminTeams();

    expect(adminTeams.length).toBe(2);
  });
});
