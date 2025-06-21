import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';
import { ITeam, ITeamPinnedMessage, IUser } from '../../../../../src/interfaces';
import { db } from '../../../../mocks/db';
import { getLoggedInUser } from '../../../../utils';
import MessageListItem from '../../../../../src/components/Teams/TeamMember/TeamPinnedMessage/MessageListItem';
import userEvent from '@testing-library/user-event';
import dayjs from 'dayjs';

const mockDeleteMutation = vi.fn(() => ({
  unwrap: () => Promise.resolve({}),
}));

vi.mock('../../../../../src/state/store', async () => {
  const actual = await vi.importActual('../../../../../src/state/store');
  return {
    ...actual,
    useDeleteTeamPinnedMessageMutation: () => [mockDeleteMutation],
  };
});

describe('MessageListItem', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = (user: IUser, overrides = {}) => {
    const team: ITeam = { ...toPlainObject(db.team.create()), userId: user.id, ...overrides };
    const message: ITeamPinnedMessage = {
      ...toPlainObject(db.teamPinnedMessage.create()),
      userId: user.id,
      fullName: 'John Doe',
    };

    return { team, message, user };
  };

  const getOwnerActions = () => {
    return {
      getGripIcon: () => screen.findByTestId('message-drag-n-drop'),
      getEditIcon: () => screen.findByTestId('edit-pinned-message-icon'),
      getDeleteIcon: () => screen.findByTestId('delete-pinned-message-icon'),
    };
  };

  const renderComponent = () => {
    const { curUser, wrapper } = getLoggedInUser();

    const props = getProps(toPlainObject(curUser));

    const ownerActions = getOwnerActions();

    render(<MessageListItem {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      props,
      ownerActions,
    };
  };

  it('should render message content, date, and editable icons when owner', async () => {
    const { props, ownerActions } = renderComponent();

    expect(screen.getByRole('heading', { level: 4, name: props.message.fullName })).toBeInTheDocument();
    expect(screen.getByText(dayjs(props.message.createdAt).format('MM/DD/YYYY'))).toBeInTheDocument();

    expect(await ownerActions.getGripIcon()).toBeInTheDocument();
    expect(await ownerActions.getEditIcon()).toBeInTheDocument();
    expect(await ownerActions.getDeleteIcon()).toBeInTheDocument();
  });

  it('should open the MessageForm when edit icon is clicked', async () => {
    const { user, ownerActions } = renderComponent();

    await user.click(await ownerActions.getEditIcon());

    expect(await screen.findByTestId('message-form')).toBeInTheDocument();
  });

  it('should call call delete mutation when unpin is clicked', async () => {
    const { user, ownerActions } = renderComponent();

    await user.click(await ownerActions.getDeleteIcon());

    await waitFor(() => {
      expect(mockDeleteMutation).toHaveBeenCalled();
    });
  });

  it('should not show edit/unpin icons if not owner', async () => {
    const { curUser, wrapper } = getLoggedInUser();
    const props = getProps(toPlainObject(curUser), { userId: 999 });

    render(<MessageListItem {...props} />, { wrapper });

    expect(screen.queryByTestId('edit-pinned-message-icon')).not.toBeInTheDocument();
    expect(screen.queryByTestId('delete-pinned-message-icon')).not.toBeInTheDocument();
    expect(screen.queryByTestId('message-drag-n-drop')).not.toBeInTheDocument();
  });
});
