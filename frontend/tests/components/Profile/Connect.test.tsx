import { screen, render, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { HttpResponse, http } from 'msw';

import Connect from '../../../src/components/Profile/Connect';
import { db } from '../../mocks/db';
import { getLoggedInUser } from '../../utils';
import { baseURL } from '../../../src/util';
import { ICreateConnectionResponse, IVerifyConnectionResponse } from '../../../src/interfaces';
import { RequestStatus } from '../../../src/enums';
import { server } from '../../mocks/server';

describe('Connect', () => {
  const createConnectionRequest = (requestStatus: string = 'success') => {
    server.use(
      http.post(`${baseURL}/connections`, () => {
        if (requestStatus === 'success') {
          return HttpResponse.json<ICreateConnectionResponse>(
            {
              message: 'success',
            },
            { status: 201 }
          );
        }
        return HttpResponse.json(
          {
            message: 'error: something went wrong',
          },
          { status: 400 }
        );
      })
    );
  };

  const verifyConnectionRequest = (status: RequestStatus = RequestStatus.UNINITIATED) => {
    server.use(
      http.get(`${baseURL}/connections/verify`, () => {
        return HttpResponse.json<IVerifyConnectionResponse>(
          {
            message: 'success',
            data: {
              id: 2,
              status,
            },
          },
          { status: 200 }
        );
      })
    );
  };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getUsers = () => {
    return {
      receiver: db.user.create({ role: 'REVIEWER' }),
      sender: db.user.create(),
    };
  };

  const getProps = (
    sender: ReturnType<typeof db.user.create>,
    receiver: ReturnType<typeof db.user.create>,
    overrides = {}
  ) => {
    return {
      receiverId: receiver.id,
      senderId: sender.id,
      fullName: receiver.fullName,
      avatarUrl: receiver.avatarUrl,
      abbreviation: receiver.abbreviation,
      ...overrides,
    };
  };

  const renderComponent = () => {
    const { sender, receiver } = getUsers();

    const props = getProps(sender, receiver);

    const { wrapper } = getLoggedInUser(sender);

    render(<Connect {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      props,
      getModalTrigger: () => screen.getByTestId('connect-modal-details'),
      getConnectButton: () => screen.findByTestId('connect-new-connect-btn'),
      getDisconnectButton: () => screen.findByTestId('connect-disconnect-btn'),
    };
  };

  it('should render connect button initially', async () => {
    renderComponent();

    expect(await screen.findByRole('button', { name: /connect/i })).toBeInTheDocument();
  });

  it('should open a modal with connection info when clicked and ask to connect', async () => {
    verifyConnectionRequest();

    const { user, props, getModalTrigger, getConnectButton } = renderComponent();

    await user.click(getModalTrigger());

    expect(await screen.findByText(`Create a connection with ${props.fullName}`)).toBeInTheDocument();
    expect(await screen.findByRole('img')).toHaveAttribute('src', props.avatarUrl);
    expect((await screen.findAllByTestId('connect-points-check')).length).toBeGreaterThan(0);
    expect(await getConnectButton()).toBeInTheDocument();
    expect(await screen.findByTestId('connect-new-cancel-btn')).toBeInTheDocument();
  });

  it('should open a modal with connection info when clicked and ask to disconnect', async () => {
    const { user, props, getModalTrigger } = renderComponent();

    await user.click(getModalTrigger());

    expect(await screen.findByText(`Do you want to disconnect with ${props.fullName}?`)).toBeInTheDocument();
    expect(await screen.findByTestId('connect-disconnect-btn')).toBeInTheDocument();
    expect(await screen.findByTestId('connect-disconnect-cancel-btn')).toBeInTheDocument();
  });

  it('should submit the connect request and close modal', async () => {
    verifyConnectionRequest();

    const { user, getModalTrigger, getConnectButton } = renderComponent();

    await user.click(getModalTrigger());

    await user.click(await getConnectButton());

    await waitFor(() => {
      expect(screen.getByTestId('connect-modal-details'));
    });
  });

  it('should show "pending..." when submit connection request is clicked', async () => {
    verifyConnectionRequest(RequestStatus.PENDING);
    const { user, getModalTrigger, getConnectButton } = renderComponent();

    await user.click(getModalTrigger());
    await user.click(await getConnectButton());

    expect(await screen.findByText(/pending/i)).toBeInTheDocument();
  });

  it('should show "connect" when disconnect is clicked', async () => {
    verifyConnectionRequest(RequestStatus.ACCEPTED);
    const { user, getModalTrigger, getDisconnectButton } = renderComponent();

    await user.click(getModalTrigger());

    expect(await screen.findByText(/connected/i)).toBeInTheDocument();
    await user.click(await getDisconnectButton());

    expect(getModalTrigger()).toBeInTheDocument();
  });

  it('should show API error messages if create connection fails', async () => {
    verifyConnectionRequest();
    createConnectionRequest('error');
    const { user, getModalTrigger, getConnectButton } = renderComponent();

    await user.click(getModalTrigger());
    await user.click(await getConnectButton());

    expect(await screen.findByText(/error:/i)).toBeInTheDocument();
  });
});
