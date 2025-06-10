import { screen, render, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import dayjs from 'dayjs';

import TeamCommentListItem from '../../../../../src/components/Teams/Post/Comment/TeamCommentListItem';
import { createTeamComments } from '../../../../mocks/dbActions';
import { getLoggedInUser } from '../../../../utils';

describe('TeamCommentListItem', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = () => {
    const [teamComment] = createTeamComments(1);

    return {
      teamComment: { ...teamComment, userId: 1 },
      updateTeamComment: vi.fn(),
      handleResetComments: vi.fn(),
    };
  };

  const getAuthorActions = () => {
    return {
      getEditIcon: () => screen.getByTestId('team-comment-list-item-edit-icon'),
      getDeleteIcon: () => screen.getByTestId('team-comment-list-item-delete-icon'),
    };
  };

  const renderComponent = (overrides = {}) => {
    const { curUser, wrapper } = getLoggedInUser(overrides);

    const props = getProps();

    render(<TeamCommentListItem {...props} />, { wrapper });
    return {
      user: userEvent.setup(),
      props,
      curUser,
      getAuthorActions,
    };
  };

  it('should render avatar, name, date, tag, and content', () => {
    const { props } = renderComponent();

    const { createdAt, content, tag, fullName, avatarUrl } = props.teamComment;

    expect(screen.getByText(content)).toBeInTheDocument();
    expect(screen.getByText(tag)).toBeInTheDocument();
    expect(screen.getByRole('img')).toHaveAttribute('src', avatarUrl);
    expect(screen.getByText(dayjs(createdAt).format('MM/DD/YYYY'))).toBeInTheDocument();
    expect(screen.getByText(fullName)).toBeInTheDocument();
  });

  it('should not show edit/delete buttons if the cur user is not the author of the comment', () => {
    renderComponent({ id: 2 });

    expect(screen.queryByTestId('team-comment-list-item-edit-icon')).not.toBeInTheDocument();
    expect(screen.queryByTestId('team-comment-list-item-delete-icon')).not.toBeInTheDocument();
  });

  it('should show edit/delete buttons if the cur user is the author of the comment', () => {
    const { getAuthorActions } = renderComponent();

    const { getEditIcon, getDeleteIcon } = getAuthorActions();

    expect(getEditIcon()).toBeInTheDocument();
    expect(getDeleteIcon()).toBeInTheDocument();
  });

  it('should open/close edit modal', async () => {
    const { user, getAuthorActions } = renderComponent();

    const { getEditIcon } = getAuthorActions();

    await user.click(getEditIcon());

    expect(await screen.findByTestId('team-modal')).toBeInTheDocument();

    await user.click(screen.getByRole('button', { name: /cancel/i }));

    expect(screen.queryByTestId('team-modal')).not.toBeInTheDocument();
  });

  it('should call the deleteMutation and reset comments', async () => {
    const { user, props, getAuthorActions } = renderComponent();

    const { getDeleteIcon } = getAuthorActions();

    await user.click(getDeleteIcon());

    await waitFor(() => {
      expect(props.handleResetComments).toHaveBeenCalled();
    });
  });
});
