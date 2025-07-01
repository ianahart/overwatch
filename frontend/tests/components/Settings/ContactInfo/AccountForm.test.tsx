import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { vi } from 'vitest';
import AccountForm from '../../../../src/components/Settings/ContactInfo/AccountForm';
import { IAccountForm, IUser, ISyncUserResponse } from '../../../../src/interfaces';
import { AllProviders } from '../../../AllProviders';
import { server } from '../../../mocks/server';
import { HttpResponse, http } from 'msw';
import { baseURL } from '../../../../src/util';
import { getLoggedInUser } from '../../../utils';
import { toPlainObject } from 'lodash';

let mockRetrieveTokens: () => { token: string } | null;

vi.mock('../../../../src/util', async (importOriginal) => {
  const actual = await importOriginal<typeof import('../../../../src/util')>();

  return {
    ...actual,
    retrieveTokens: () => mockRetrieveTokens(),
  };
});

describe('AccountForm', () => {
  beforeEach(() => {
    vi.clearAllMocks();

    mockRetrieveTokens = () => ({ token: 'fake-token' });
  });

  const getForm = () => {
    return {
      getFirstNameInput: () => screen.getByLabelText(/first name/i),
      getLastNameInput: () => screen.getByLabelText(/last name/i),
      getEmailInput: () => screen.getByLabelText(/email/i),
      getUpdateBtn: () => screen.getByRole('button', { name: /update/i }),
      getCancelBtn: () => screen.getByRole('button', { name: /cancel/i }),
    };
  };

  const getProps = () => {
    const form: IAccountForm = {
      firstName: { name: 'firstName', value: '', error: '', type: 'text' },
      lastName: { name: 'lastName', value: '', error: '', type: 'text' },
      email: { name: 'email', value: '', error: '', type: 'email' },
    };

    return {
      handleUpdateField: vi.fn(),
      handleSubmit: vi.fn(),
      cancelUpdate: vi.fn(),
      form,
    };
  };

  const renderComponent = () => {
    const props = getProps();

    render(<AccountForm {...props} />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      props,
      form: getForm(),
    };
  };

  it('should render form fields and buttons', () => {
    const { form } = renderComponent();

    const { getCancelBtn, getUpdateBtn, getEmailInput, getLastNameInput, getFirstNameInput } = form;

    expect(getFirstNameInput()).toBeInTheDocument();
    expect(getLastNameInput()).toBeInTheDocument();
    expect(getEmailInput()).toBeInTheDocument();
    expect(getUpdateBtn()).toBeInTheDocument();
    expect(getCancelBtn()).toBeInTheDocument();
  });

  it('should auto-populate form fields when sync query succeeds', async () => {
    const { curUser } = getLoggedInUser();

    const plainUser: IUser = toPlainObject(curUser);

    server.use(
      http.get(`${baseURL}/users/sync`, () => {
        return HttpResponse.json<ISyncUserResponse>(plainUser, { status: 200 });
      })
    );

    const { props } = renderComponent();

    await waitFor(() => {
      expect(props.handleUpdateField).toHaveBeenCalledWith('email', plainUser.email, 'value');
      expect(props.handleUpdateField).toHaveBeenCalledWith('firstName', plainUser.firstName, 'value');
      expect(props.handleUpdateField).toHaveBeenCalledWith('lastName', plainUser.lastName, 'value');
      expect(props.handleUpdateField).toHaveBeenCalledWith('id', plainUser.id.toString(), 'value');
    });
  });

  it('should call "handleSubmit" on form submit', async () => {
    const { user, props, form } = renderComponent();

    await user.click(form.getUpdateBtn());

    expect(props.handleSubmit).toHaveBeenCalled();
  });

  it('should call "cancelUpdate" on cancel button click', async () => {
    const { user, props, form } = renderComponent();

    await user.click(form.getCancelBtn());

    expect(props.cancelUpdate).toHaveBeenCalled();
  });
});
