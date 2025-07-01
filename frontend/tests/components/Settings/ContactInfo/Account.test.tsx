import { screen, render, waitFor } from '@testing-library/react';
import { useDispatch } from 'react-redux';
import { Mock } from 'vitest';
import { mockNavigate } from '../../../setup';

import Account from '../../../../src/components/Settings/ContactInfo/Account';
import { getLoggedInUser } from '../../../utils';
import userEvent from '@testing-library/user-event';
import { baseURL } from '../../../../src/util';
import { server } from '../../../mocks/server';
import { HttpResponse, http } from 'msw';

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

describe('Account', () => {
  const mockDispatch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const getIcons = () => {
    return {
      openIcon: () => screen.getByTestId('account-chevron-left'),
      closeIcon: () => screen.getByTestId('account-chevron-down'),
    };
  };

  const renderComponent = () => {
    const { wrapper, curUser } = getLoggedInUser();

    render(<Account />, { wrapper });

    return {
      user: userEvent.setup(),
      curUser,
      icons: getIcons(),
    };
  };

  it('should render account info section initially', async () => {
    const { curUser } = renderComponent();

    expect(await screen.findByText(/account/i)).toBeInTheDocument();
    expect(await screen.findByText(curUser.firstName)).toBeInTheDocument();
    expect(await screen.findByText(curUser.lastName)).toBeInTheDocument();
    expect(screen.queryByPlaceholderText(/enter your first name/i)).not.toBeInTheDocument();
  });

  it('should toggle form on icon click', async () => {
    const { user, icons } = renderComponent();

    await user.click(icons.openIcon());

    expect(screen.getByPlaceholderText(/enter your first name/i)).toBeInTheDocument();
  });

  it('should show validation errors if fields are empty', async () => {
    const { user, icons } = renderComponent();

    await user.click(icons.openIcon());

    const updateBtn = screen.getByRole('button', { name: /update/i });

    await user.click(updateBtn);

    expect(await screen.findAllByText(/field must not be empty/i)).toHaveLength(3);
  });

  it('should submit valid form and navigates on success', async () => {
    const { user, icons } = renderComponent();

    await user.click(icons.openIcon());

    await user.type(screen.getByLabelText(/first name/i), 'Alice');
    await user.type(screen.getByLabelText(/last name/i), 'Smith');
    await user.type(screen.getByLabelText(/email/i), 'alice@example.com');

    await user.click(screen.getByRole('button', { name: /update/i }));

    await waitFor(() => {
      const calls = mockDispatch.mock.calls.map(([arg]) => arg.type);
      expect(calls).toContain('user/clearUser');
      expect(calls).toContain('setting/clearSetting');
      expect(mockNavigate).toHaveBeenCalledWith('/signin');
    });
  });
  it('should apply server errors from the API', async () => {
    server.use(
      http.patch(`${baseURL}/users/:userId`, () => {
        return HttpResponse.json(
          {
            message: 'server error is present',
          },
          { status: 400 }
        );
      })
    );
    const { user, icons } = renderComponent();

    await user.click(icons.openIcon());

    await user.type(screen.getByLabelText(/first name/i), 'Alice');
    await user.type(screen.getByLabelText(/last name/i), 'Smith');
    await user.type(screen.getByLabelText(/email/i), 'alice@example.com');

    await user.click(screen.getByRole('button', { name: /update/i }));

    expect(await screen.findByText('server error is present')).toBeInTheDocument();
  });
});
