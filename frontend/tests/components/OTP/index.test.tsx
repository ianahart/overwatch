import { screen, render, waitFor } from '@testing-library/react';

import OTP from '../../../src/components/OTP';
import { getLoggedInUser } from '../../utils';
import userEvent from '@testing-library/user-event';
import { mockLocation, mockNavigate } from '../../setup';
import { baseURL } from '../../../src/util';
import { HttpResponse, http } from 'msw';
import { server } from '../../mocks/server';

vi.mock('../../../state/store', async () => {
  const actual = await vi.importActual<typeof import('../../../src/state/store')>('../../../src/state/store');
  return {
    ...actual,
    updateUserAndTokens: vi.fn(() => ({ type: 'user/updateUserAndTokens' })),
  };
});

describe('OTP', () => {
  beforeEach(() => {
    vi.clearAllMocks();

    mockLocation.mockReturnValue({
      state: { userId: 1 },
      pathname: '/otp',
      search: '',
      hash: '',
      key: 'test-key',
    });
  });

  const renderComponent = () => {
    const { wrapper } = getLoggedInUser();

    render(<OTP />, { wrapper });

    return {
      user: userEvent.setup(),
      getInput: () => screen.getByRole('textbox'),
      getSubmitButton: () => screen.getByRole('button', { name: /proceed/i }),
    };
  };

  it('should render input and submit button', () => {
    const { getInput, getSubmitButton } = renderComponent();

    expect(getInput()).toBeInTheDocument();
    expect(getSubmitButton()).toBeInTheDocument();
  });

  it('should not submit if the input is empty', async () => {
    const { getSubmitButton, user } = renderComponent();

    await user.click(getSubmitButton());

    expect(screen.queryByText(/error/i)).not.toBeInTheDocument();
  });

  it('should verify the OTP and navigate to dashboard on success', async () => {
    const { user, getSubmitButton, getInput } = renderComponent();

    await user.type(getInput(), '123456');
    await user.click(getSubmitButton());

    await waitFor(() => {
      expect(mockNavigate).toHaveBeenCalledWith(`/dashboard/slug`);
    });
  });

  it('should show error on failed verification', async () => {
    server.use(
      http.post(`${baseURL}/auth/verify-otp`, () => {
        return HttpResponse.json(
          {
            message: 'Invalid OTP',
          },
          { status: 400 }
        );
      })
    );
    const { user, getInput, getSubmitButton } = renderComponent();

    await user.type(getInput(), '123456');
    await user.click(getSubmitButton());

    expect(await screen.findByText(/invalid otp/i)).toBeInTheDocument();
  });
});
