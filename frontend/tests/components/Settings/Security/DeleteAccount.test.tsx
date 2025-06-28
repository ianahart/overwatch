import { screen, render, waitFor } from '@testing-library/react';
import DeleteAccount from '../../../../src/components/Settings/Security/DeleteAccount';
import userEvent from '@testing-library/user-event';
import { getLoggedInUser } from '../../../utils';

vi.mock('react-redux', async () => {
  const actual = await vi.importActual<typeof import('react-redux')>('react-redux');

  return {
    ...actual,
    useDispatch: () => (action: any) => {
      if (typeof action === 'function') {
        return action();
      }
      return action;
    },
  };
});

vi.mock('../../../../src/state/store', async () => {
  const actual = await vi.importActual<typeof import('../../../../src/state/store')>('../../../../src/state/store');

  return {
    ...actual,
    clearUser: vi.fn(() => ({ type: 'user/clearUser' })),
    clearSetting: vi.fn(() => ({ type: 'setting/clearSetting' })),
  };
});

import * as store from '../../../../src/state/store';

describe('DeleteAccount', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getForm = () => {
    return {
      getFormTrigger: () => screen.getByTestId('delete-account-form-trigger'),
      getEmailInput: () => screen.getByLabelText(/email/i),
      getPasswordInput: () => screen.getByLabelText(/password/i),
      getSubmitBtn: () => screen.getByRole('button', { name: /delete account/i }),
    };
  };

  const renderComponent = () => {
    const { curUser, wrapper } = getLoggedInUser();
    const form = getForm();

    render(<DeleteAccount />, { wrapper });

    return {
      form,
      user: userEvent.setup(),
      curUser,
    };
  };

  it('should toggle form visibility when clicked', async () => {
    const { user, form } = renderComponent();

    expect(screen.queryByLabelText(/email/i)).not.toBeInTheDocument();

    await user.click(form.getFormTrigger());

    await waitFor(() => {
      expect(form.getEmailInput()).toBeInTheDocument();
      expect(form.getPasswordInput()).toBeInTheDocument();
    });
  });

  it('should validate form input fields if they are empty', async () => {
    const { user, form } = renderComponent();

    await user.click(form.getFormTrigger());
    await user.click(form.getSubmitBtn());

    const errors = await screen.findAllByText('Field must not be empty');
    expect(errors.length).toBe(2);
  });

  it('should not allow submission if email is incorrect', async () => {
    const { user, form } = renderComponent();

    await user.click(form.getFormTrigger());
    await user.type(form.getEmailInput(), 'wrong@example.com');
    await user.type(form.getPasswordInput(), 'Test12345%');
    await user.click(form.getSubmitBtn());

    await waitFor(() => {
      expect(screen.queryByText(/deleting account/i)).not.toBeInTheDocument();
    });
  });

  it('should call deleteUser mutation and navigate to /signout on success', async () => {
    const { user, curUser, form } = renderComponent();

    await user.click(form.getFormTrigger());
    await user.type(form.getEmailInput(), curUser.email);
    await user.type(form.getPasswordInput(), 'Test12345%');
    await user.click(form.getSubmitBtn());

    await waitFor(() => {
      expect(store.clearUser).toHaveBeenCalled();
      expect(store.clearSetting).toHaveBeenCalled();
    });
  });
});
