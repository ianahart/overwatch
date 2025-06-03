import { screen, render, waitFor } from '@testing-library/react';
import { HttpResponse, http } from 'msw';
import userEvent from '@testing-library/user-event';

import Disconnect from '../../../src/components/Profile/Disconnect';
import { AllProviders } from '../../AllProviders';
import { RequestStatus } from '../../../src/enums';
import { server } from '../../mocks/server';
import { baseURL } from '../../../src/util';
import { IVerifyConnectionResponse } from '../../../src/interfaces';

describe('Disconnect', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

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

  const getProps = () => {
    return {
      receiverId: 1,
      senderId: 2,
    };
  };

  const renderComponent = () => {
    const props = getProps();

    render(<Disconnect {...props} />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      getConnectButton: () => screen.findByRole('button', { name: /connected/i }),
    };
  };

  it('should render button when status is "ACCEPTED"', async () => {
    const { getConnectButton } = renderComponent();

    expect(await getConnectButton()).toBeInTheDocument();
  });

  it('should delete connection when connected is clicked', async () => {
    const { user, getConnectButton } = renderComponent();

    await user.click(await getConnectButton());

    waitFor(() => {
      expect(screen.queryByRole('button', { name: /connected/i })).not.toBeInTheDocument();
    });
  });

  it('should not render button if status is not "ACCEPTED"', async () => {
    verifyConnectionRequest(RequestStatus.UNINITIATED);

    renderComponent();

    waitFor(() => {
      expect(screen.queryByRole('button', { name: /connected/i })).not.toBeInTheDocument();
    });
  });
});
