import { screen, render, waitFor } from '@testing-library/react';
import { Mock } from 'vitest';
import { useDispatch } from 'react-redux';

import AvatarUpload from '../../../../src/components/Settings/EditProfile/AvatarUpload';
import { getLoggedInUser } from '../../../utils';
import { IFormField } from '../../../../src/interfaces';
import userEvent from '@testing-library/user-event';
import { updateAvatar } from '../../../../src/state/store';

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

describe('AvatarUpload', () => {
  const mockDispatch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const renderComponent = () => {
    const avatar: IFormField<string | File | null> = {
      name: '',
      type: '',
      error: '',
      value: 'https://imgur.com/original',
    };

    const { curUser, wrapper } = getLoggedInUser(
      {},
      {
        profileSetup: {
          avatar,
        },
      }
    );

    render(<AvatarUpload />, { wrapper });

    return {
      user: userEvent.setup(),
      getInput: () => screen.getByLabelText(/avatar upload/i),
      avatar,
      curUser,
    };
  };

  it('should render avatar upload UI', () => {
    const { getInput } = renderComponent();

    expect(screen.getByText(/avatar/i)).toBeInTheDocument();
    expect(getInput()).toBeInTheDocument();
  });

  it('should show error if uploaded file exceeds 2MB', async () => {
    const { user, getInput } = renderComponent();

    const file = new File(['x'.repeat(2_000_001)], 'big-avatar.png', { type: 'image/png' });

    await user.upload(getInput(), file);

    expect(await screen.findByText(/exceeds 2MB/i)).toBeInTheDocument();
    expect(mockDispatch).not.toHaveBeenCalled();
  });

  it('should dispatch updateAvatar for valid file upload', async () => {
    const { user, getInput } = renderComponent();

    const file = new File(['avatar content'], 'avatar.png', { type: 'image/png' });

    await user.upload(getInput(), file);

    expect(mockDispatch).toHaveBeenCalledWith(updateAvatar(file));
  });

  it('should dispatch removeAvatar and updateUser when "Remove" is clicked', async () => {
    const { user } = renderComponent();

    const btn = screen.getByRole('button', { name: /remove/i });
    await user.click(btn);

    await waitFor(() => {
      expect(mockDispatch).toHaveBeenCalledWith(updateAvatar(null));

      const updateUserCall = mockDispatch.mock.calls.find(([action]) => action.type === 'user/updateUser');

      expect(updateUserCall?.[0].payload.avatarUrl).toBe('');
    });
  });
});
