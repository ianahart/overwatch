import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';
import userEvent from '@testing-library/user-event';

import { IConnection, IPinnedConnection } from '../../../../src/interfaces';
import { db } from '../../../mocks/db';
import { getLoggedInUser } from '../../../utils';
import Connection from '../../../../src/components/Settings/Connects/Connection';
import { shortenString } from '../../../../src/util';

type TConnection = IConnection | IPinnedConnection;

describe('Connection', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getIcons = () => {
    return {
      getBlockIcon: () => screen.getByTestId('connection-block-icon'),
      getPinIconAngle: () => screen.getByTestId('connection-pin-icon-angle'),
      getPinIconCirlce: () => screen.getByTestId('connection-pin-icon-circle'),
    };
  };

  const getProps = (overrides = {}) => {
    const connection: TConnection = {
      ...toPlainObject(db.connection.create()),
      senderId: 1,
      receiverId: 2,
      connectionPinId: 1,
    };

    return {
      connection,
      changeConnection: vi.fn(),
      isPinned: false,
      unPin: vi.fn(),
      pin: vi.fn(),
      blockUser: vi.fn(),
      ...overrides,
    };
  };

  const renderComponent = (overrides = {}) => {
    const props = getProps(overrides);

    const { curUser, wrapper } = getLoggedInUser(
      {},
      {
        chat: {
          currentConnection: props.connection,
        },
      }
    );

    render(<Connection {...props} />, { wrapper });

    return {
      props,
      curUser,
      user: userEvent.setup(),
      icons: getIcons(),
    };
  };

  it('should render with correct name and message', () => {
    const { props } = renderComponent();

    const { connection } = props;

    expect(screen.getByText(`${connection.firstName} ${connection.lastName}`)).toBeInTheDocument();
    expect(
      screen.getByText(shortenString(connection.lastMessage ? connection.lastMessage : '', 5))
    ).toBeInTheDocument();
  });

  it('should call "changeConnection" on click', async () => {
    const { user, props } = renderComponent();

    await user.click(screen.getByTestId('connection-avatar'));

    await waitFor(() => {
      expect(props.changeConnection).toHaveBeenCalledWith(props.connection);
    });
  });

  it('should show block tooltip on hover', async () => {
    const { user, icons } = renderComponent();

    await user.hover(icons.getBlockIcon());

    expect(await screen.findByText(/block/i)).toBeInTheDocument();
  });

  it('should call "blockUser" when clicked', async () => {
    const { user, icons, props } = renderComponent();

    await user.click(icons.getBlockIcon());

    await waitFor(() => {
      const { connection } = props;

      expect(props.blockUser).toHaveBeenCalledWith(connection.senderId, connection.receiverId, connection);
    });
  });

  it('should call "unPin" when clicked', async () => {
    const { user, icons, props } = renderComponent({ isPinned: true });

    await user.click(icons.getPinIconAngle());

    await waitFor(() => {
      // @ts-ignore
      expect(props.unPin).toHaveBeenCalledWith(props.connection.connectionPinId);
    });
  });

  it('should call "pin" when clicked', async () => {
    const { curUser, user, icons, props } = renderComponent({ isPinned: false });

    await user.click(icons.getPinIconCirlce());

    await waitFor(() => {
      // @ts-ignore
      expect(props.pin).toHaveBeenCalledWith(
        curUser.id,
        props.connection.id,
        props.connection.receiverId,
        'mocked-token',
        props.connection
      );
    });
  });
});
