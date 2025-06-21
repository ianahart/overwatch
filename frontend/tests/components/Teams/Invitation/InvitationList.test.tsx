import { screen, render } from '@testing-library/react';
import { faker } from '@faker-js/faker';
import { toPlainObject } from 'lodash';

import InvitationList from '../../../../src/components/Teams/Invitation/InvitationList';
import userEvent from '@testing-library/user-event';
import { getWrapper } from '../../../RenderWithProviders';
import { db } from '../../../mocks/db';
import { ITeamInvitiation } from '../../../../src/interfaces';

describe('InvitationList', () => {
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
      getLoadMoreBtn: () => screen.getByRole('button', { name: /load more/i }),
    };
  };

  const renderComponent = (overrides = {}) => {
    const props = getProps();

    const wrapper = getWrapper({
      user: {
        user: props.curUser,
        token: props.curUser.token,
      },
      team: {
        teamInvitationPagination: {
          page: -1,
          pageSize: 2,
          totalPages: 2,
          direction: 'next',
          totalElements: 4,
          ...overrides,
        },
        teamInvitations: [],
      },
    } as any);

    render(<InvitationList />, { wrapper });

    return {
      user: userEvent.setup(),
      props,
    };
  };

  it('should not render the load more button if on last page', () => {
    renderComponent({ page: 1, totalPages: 1 });

    expect(screen.queryByRole('button', { name: /load more/i })).not.toBeInTheDocument();
  });
});
