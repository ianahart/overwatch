import { render, screen, waitFor } from '@testing-library/react';
import { HttpResponse, http } from 'msw';
import userEvent from '@testing-library/user-event';

import ChangePassword from '../../../../src/components/Settings/Security/ChangePassword';
import { getLoggedInUser } from '../../../utils';
import { server } from '../../../mocks/server';
import { baseURL } from '../../../../src/util';
import * as store from '../../../../src/state/store';

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

describe('ChangePassword', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getForm = () => {
    return {
      getFormTrigger: () => screen.getByTestId('toggle-password-trigger'),
      getCurrentPasswordInput: () => screen.getByLabelText(/current password/i),
      getNewPasswordInput: () => screen.getByLabelText(/new password/i),
      getSubmitBtn: () => screen.getByRole('button', { name: /change password/i }),
    };
  };

  const renderComponent = () => {
    const { curUser, wrapper } = getLoggedInUser();

    const form = getForm();

    render(<ChangePassword />, { wrapper });

    return {
      curUser,
      user: userEvent.setup(),
      form,
    };
  };

  it('should render collapsed by default and toggle form on click', async () => {
    const { form, user } = renderComponent();

    const { getFormTrigger, getNewPasswordInput, getCurrentPasswordInput } = form;

    expect(screen.queryByLabelText(/current password/i)).not.toBeInTheDocument();

    await user.click(getFormTrigger());

    expect(getNewPasswordInput()).toBeInTheDocument();
    expect(getCurrentPasswordInput()).toBeInTheDocument();
  });

  it('should show validation errors on empty input fields', async () => {
    const { form, user } = renderComponent();

    const { getFormTrigger, getNewPasswordInput, getCurrentPasswordInput } = form;

    await user.click(getFormTrigger());

    await user.click(getCurrentPasswordInput());
    await user.tab();

    await user.click(getNewPasswordInput());
    await user.tab();

    expect(await screen.findByText('Password must be between 5 and 50 characters')).toBeInTheDocument();
    expect(await screen.findByText('New password must be between 5 and 50 characters')).toBeInTheDocument();
  });

  it('should call update password mutation and sign out on success', async () => {
    const signOutCalled = vi.fn();
    const clearUserSpy = vi.spyOn(store, 'clearUser');

    server.use(
      http.post(`${baseURL}/auth/logout`, () => {
        signOutCalled();
        return HttpResponse.json({ message: 'success' });
      })
    );

    const { form, user } = renderComponent();

    const { getSubmitBtn, getFormTrigger, getNewPasswordInput, getCurrentPasswordInput } = form;

    await user.click(getFormTrigger());

    await user.type(getCurrentPasswordInput(), 'Test12345%');
    await user.type(getNewPasswordInput(), 'Test123456%');

    await user.click(getSubmitBtn());

    await waitFor(() => {
      expect(signOutCalled).toHaveBeenCalled();
      expect(clearUserSpy).toHaveBeenCalled();
    });
  });

  it('should render errors on failed API update password request', async () => {
    server.use(
      http.patch(`${baseURL}/users/:userId/password`, () => {
        return HttpResponse.json({ message: 'password cannot be the same as your old password' }, { status: 400 });
      })
    );

    const { form, user } = renderComponent();

    const { getSubmitBtn, getFormTrigger, getNewPasswordInput, getCurrentPasswordInput } = form;

    await user.click(getFormTrigger());

    await user.type(getCurrentPasswordInput(), 'Test12345%');
    await user.type(getNewPasswordInput(), 'Test12345%');

    await user.click(getSubmitBtn());

    await waitFor(() => {
      expect(screen.getByText('password cannot be the same as your old password')).toBeInTheDocument();
    });
  });
});
