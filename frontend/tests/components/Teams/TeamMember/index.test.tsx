import { screen, render } from '@testing-library/react';
import { setMockParams } from '../../../setup';
import { getLoggedInUser } from '../../../utils';
import TeamMember from '../../../../src/components/Teams/TeamMember';

describe('TeamMember', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    setMockParams({ teamId: '1' });
  });

  const renderComponent = () => {
    const { wrapper } = getLoggedInUser();

    render(<TeamMember />, { wrapper });
  };

  it('should render the team pinned message', () => {
    renderComponent();

    expect(screen.getByTestId('team-pinned-message')).toBeInTheDocument();
  });

  it('should render the team member list', () => {
    renderComponent();

    expect(screen.getByTestId('team-member-list')).toBeInTheDocument();
  });
});
