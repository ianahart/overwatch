import { screen, render, waitFor } from '@testing-library/react';

import TeamMemberItem, { ITeamMemberItemProps } from '../../../../src/components/Teams/TeamMember/TeamMemberItem';
import { getLoggedInUser } from '../../../utils';
import { db } from '../../../mocks/db';
import { toPlainObject } from 'lodash';
import { ITeamMember } from '../../../../src/interfaces';
import userEvent from '@testing-library/user-event';
import dayjs from 'dayjs';

describe('TeamMemberItem', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = (overrides = {}) => {
    const teamMember: ITeamMember = { ...toPlainObject(db.teamMember.create()), userId: 2 };

    return {
      teamMember,
      adminUserId: 1,
      isAdmin: false,
      handleDeleteTeamMember: vi.fn(),
      ...overrides,
    };
  };

  const getElements = (props: ITeamMemberItemProps) => {
    return {
      getAvatar: () => screen.getByRole('img'),
      getFullName: () => screen.getByText(props.teamMember.fullName),
      getRole: () => screen.getByText(/admin|team member/i),
      getJoinedDate: () => screen.getByText(`Joined ${dayjs(props.teamMember.createdAt).format('MM/DD/YYYY')}`),
      getRemoveIcon: () => screen.getByTestId('remove-team-member-icon'),
    };
  };

  const renderComponent = () => {
    const { curUser, wrapper } = getLoggedInUser();

    const props = getProps({ adminUserId: curUser.id });

    const elements = getElements(props);

    render(<TeamMemberItem {...props} />, { wrapper });

    return {
      props,
      curUser,
      user: userEvent.setup(),
      elements,
    };
  };

  it('should render name, avatar, role, and join date correctly', () => {
    const { elements } = renderComponent();

    const { getRole, getAvatar, getFullName, getJoinedDate } = elements;

    expect(getRole()).toBeInTheDocument();
    expect(getFullName()).toBeInTheDocument();
    expect(getAvatar()).toBeInTheDocument();
    expect(getJoinedDate()).toBeInTheDocument();
  });

  it('should render "Admin" badge if member is admin', () => {
    const { curUser, wrapper } = getLoggedInUser();

    const props = getProps({ AdminUserId: curUser.id, isAdmin: true });

    render(<TeamMemberItem {...props} />, { wrapper });

    const { getRole } = getElements(props);

    expect(getRole()).toHaveTextContent('Admin');
    expect(getRole()).toHaveClass('text-green-400');
  });

  it('should render "Team Member" if member is not an admin', () => {
    const { elements } = renderComponent();

    const { getRole } = elements;

    expect(getRole()).toHaveTextContent('Team Member');
    expect(getRole()).toHaveClass('text-blue-400');
  });

  it('should render delete icon and call "handleDeleteTeamMember" when clicked by an admin', async () => {
    const { curUser, wrapper } = getLoggedInUser();

    const user = userEvent.setup();

    const props = getProps({ AdminUserId: curUser.id, isAdmin: false });

    render(<TeamMemberItem {...props} />, { wrapper });

    const { getRemoveIcon } = getElements(props);

    await user.click(getRemoveIcon());

    await waitFor(() => {
      expect(props.handleDeleteTeamMember).toHaveBeenCalledWith(props.teamMember.id);
    });
  });

  it('should not show delete icon if user is not admin', () => {
    const { curUser, wrapper } = getLoggedInUser();

    // isAdmin is the teamember NOT the currentuser
    const props = getProps({ AdminUserId: curUser.id, isAdmin: true });

    render(<TeamMemberItem {...props} />, { wrapper });

    expect(screen.queryByTestId('remove-team-member-icon')).not.toBeInTheDocument();
  });
});
