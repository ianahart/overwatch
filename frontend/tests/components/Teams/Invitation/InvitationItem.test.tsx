import { screen, render, waitFor } from '@testing-library/react';
import { faker } from '@faker-js/faker';
import { toPlainObject } from 'lodash';
import userEvent from '@testing-library/user-event';
import dayjs from 'dayjs';

import InvitationItem from '../../../../src/components/Teams/Invitation/InvitationItem';
import { getWrapper } from '../../../RenderWithProviders';
import { db } from '../../../mocks/db';
import { ITeamInvitiation } from '../../../../src/interfaces';

const mockUpdate = vi.fn().mockReturnValue({ unwrap: () => Promise.resolve({}) });
const mockDelete = vi.fn().mockReturnValue({
  unwrap: () => Promise.resolve({}),
});

vi.mock('../../../../src/state/store', async () => {
  const actual = await vi.importActual<typeof import('../../../../src/state/store')>('../../../../src/state/store');
  return {
    ...actual,
    useUpdateTeamInvitationMutation: () => [mockUpdate],
    useDeleteTeamInvitationMutation: () => [mockDelete],
    useLazyFetchTeamMemberTeamsQuery: () => [vi.fn()],
  };
});

describe('InvitationItem', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    db.teamInvitation.deleteMany({ where: { id: { lte: 999 } } });
  });

  const createCurUser = () => {
    return {
      ...db.user.create(),
      id: 1,
      token: faker.lorem.word(),
    };
  };

  const getProps = () => {
    const curUser = createCurUser();
    const teamInvitation: ITeamInvitiation = {
      ...toPlainObject(db.teamInvitation.create()),
      receiverId: curUser.id,
      teamId: 1,
      senderId: 2,
    };

    return {
      teamInvitation,
      curUser,
      getAcceptBtn: () => screen.getByRole('button', { name: /accept/i }),
      getIgnoreBtn: () => screen.getByRole('button', { name: /ignore/i }),
    };
  };

  const renderComponent = () => {
    const props = getProps();

    const wrapper = getWrapper({
      user: {
        user: props.curUser,
        token: props.curUser.token,
      },
      team: {
        teamMemberTeamPagination: {
          page: 0,
          pageSize: 2,
          totalPages: 0,
          direction: 'next',
          totalElements: 0,
        },
        teamInvitations: [props.teamInvitation],
      },
    } as any);

    render(<InvitationItem teamInvitation={props.teamInvitation} />, { wrapper });

    return {
      user: userEvent.setup(),
      props,
    };
  };

  it('should render the sender name and formatted date', () => {
    const { props } = renderComponent();

    expect(screen.getByRole('heading', { level: 3, name: props.teamInvitation.senderFullName })).toBeInTheDocument();
    expect(screen.getByText(dayjs(props.teamInvitation.createdAt).format('MM/DD/YYYY'))).toBeInTheDocument();
  });

  it('should render invitation message and team name', () => {
    const { props } = renderComponent();

    expect(screen.getByText(/has sent you a team inivitation to join a team called/i)).toBeInTheDocument();
    expect(screen.getByText(props.teamInvitation.teamName)).toBeInTheDocument();
  });

  it('should render accept and ignore button', () => {
    const { props } = renderComponent();
    const { getAcceptBtn, getIgnoreBtn } = props;

    expect(getAcceptBtn()).toBeInTheDocument();
    expect(getIgnoreBtn()).toBeInTheDocument();
  });

  it('should update on accept button click', async () => {
    const { user, props } = renderComponent();

    await user.click(props.getAcceptBtn());

    await waitFor(() => {
      expect(mockUpdate).toHaveBeenCalledWith({
        token: expect.any(String),
        teamInvitationId: props.teamInvitation.id,
        teamId: props.teamInvitation.teamId,
        userId: props.teamInvitation.receiverId,
      });
    });
  });

  it('should delete on ignore button click', async () => {
    const { user, props } = renderComponent();

    await user.click(props.getIgnoreBtn());

    await waitFor(() => {
      expect(mockDelete).toHaveBeenCalledWith({
        token: expect.any(String),
        teamInvitationId: props.teamInvitation.id,
      });
    });
  });
});
