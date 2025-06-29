import { screen, render, waitFor } from '@testing-library/react';

import BlockedUserItem from '../../../../src/components/Settings/Profile/BlockedUserItem';
import { getLoggedInUser } from '../../../utils';
import { IBlockedUser } from '../../../../src/interfaces';
import { toPlainObject } from 'lodash';
import { db } from '../../../mocks/db';
import userEvent from '@testing-library/user-event';
import dayjs from 'dayjs';
import { HttpResponse, http } from 'msw';
import { baseURL } from '../../../../src/util';
import { server } from '../../../mocks/server';

describe('BlockedUserItem', () => {
  const getProps = () => {
    const blockedUser: IBlockedUser = { ...toPlainObject(db.blockedUser.create()), blockedUserId: 2 };

    return {
      blockedUser,
    };
  };

  const renderComponent = () => {
    const { wrapper } = getLoggedInUser();

    const props = getProps();

    render(<BlockedUserItem {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      props,
    };
  };

  it('should render the blocked user full name and formatted date', () => {
    const { props } = renderComponent();

    expect(screen.getByText(props.blockedUser.fullName)).toBeInTheDocument();
    expect(screen.getByText(dayjs(props.blockedUser.createdAt).format('MM/DD/YYYY'))).toBeInTheDocument();
  });

  it('should show tooltip on hover', async () => {
    const { user } = renderComponent();

    const unblockIcon = screen.getByTestId('blocked-user-tooltip');

    await user.hover(unblockIcon);

    await waitFor(() => {
      expect(screen.getByText('Unblock')).toBeInTheDocument();
    });
  });
  it('should call the unblock mutation on click', async () => {
    const handler = vi.fn(() => HttpResponse.json({ message: 'success' }));

    server.use(http.delete(`${baseURL}/block-users/:blockUserId`, handler));

    const { user } = renderComponent();

    const unblockIcon = screen.getByTestId('blocked-user-tooltip');

    await user.click(unblockIcon);

    await waitFor(() => {
      expect(handler).toHaveBeenCalled();
    });
  });
});
