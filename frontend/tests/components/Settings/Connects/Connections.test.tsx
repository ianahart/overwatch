import { screen, render } from '@testing-library/react';
import { toPlainObject } from 'lodash';

import Connections from '../../../../src/components/Settings/Connects/Connections';
import { IConnection, IPinnedConnection } from '../../../../src/interfaces';
import { db } from '../../../mocks/db';
import { getLoggedInUser } from '../../../utils';
import userEvent from '@testing-library/user-event';

vi.mock('../../../../src/util', async () => {
  const actual = await vi.importActual<typeof import('../../../../src/util')>('../../../../src/util');
  return {
    ...actual,
    retrieveTokens: () => ({ token: 'mock-token' }),
  };
});

describe('Connections', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getForm = () => {
    return {
      getSearchInput: () => screen.getByPlaceholderText(/search/i),
    };
  };

  const getConnections = () => {
    const connections: IConnection[] = [
      { ...toPlainObject(db.connection.create()), receiverId: 1, senderId: 2, firstName: 'Ian', lastName: 'Hart' },
    ];

    const pinnedConnections: IPinnedConnection[] = [
      { ...toPlainObject(db.connection.create()), receiverId: 3, senderId: 2, firstName: 'Colin', lastName: 'Hart' },
    ];

    return {
      connections,
      pinnedConnections,
      currentConnection: connections[0],
    };
  };

  const renderComponent = () => {
    const { connections, pinnedConnections, currentConnection } = getConnections();

    const { curUser, wrapper } = getLoggedInUser(
      {},
      {
        chat: {
          connections,
          currentConnection,
          pinnedConnections,
        },
      }
    );

    render(<Connections />, { wrapper });

    return {
      curUser,
      connections,
      form: getForm(),
      currentConnection,
      pinnedConnections,
      user: userEvent.setup(),
    };
  };

  it('should render the search input', () => {
    const { form } = renderComponent();

    expect(form.getSearchInput()).toBeInTheDocument();
  });

  it('should render a list of fetched connections', () => {
    renderComponent();

    const connections = screen.getAllByTestId('settings-connection-item');

    expect(connections.length).toBe(2);
  });

  it('should render pinned connections when available', () => {
    const { pinnedConnections } = renderComponent();

    for (const pc of pinnedConnections) {
      expect(screen.getByText(`${pc.firstName} ${pc.lastName}`)).toBeInTheDocument();
    }
  });

  it('should show "See more..." if pagination is enabled', async () => {
    renderComponent();

    expect(await screen.findByRole('button', { name: /see more/i })).toBeInTheDocument();
  });

  it('should trigger pagination when "See more..." is clicked', async () => {
    const { user } = renderComponent();

    await user.click(await screen.findByRole('button', { name: /see more/i }));

    const connections = await screen.findAllByTestId('settings-connection-item');

    expect(connections.length).toBe(8);
  });
});
