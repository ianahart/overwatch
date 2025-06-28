import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';

import * as store from '../../../../src/state/store';
import MultiFactorAuthentication from '../../../../src/components/Settings/Security/MultiFactorAuthentication';
import { getLoggedInUser } from '../../../utils';
import { db } from '../../../mocks/db';
import userEvent from '@testing-library/user-event';
import { ISetting } from '../../../../src/interfaces';

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: () => vi.fn(),
  };
});

describe('MultiFactorAuthentication', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getForm = () => {
    return {
      getFormTrigger: () => screen.getByTestId('mf-trigger'),
      getSwitch: () => screen.getByTestId('settings-switch'),
    };
  };

  const renderComponent = (overrides = {}) => {
    const setting: ISetting = { ...toPlainObject(db.setting.create()), ...overrides };

    const { wrapper } = getLoggedInUser(
      {},
      {
        setting: {
          setting,
        },
      }
    );

    const form = getForm();

    render(<MultiFactorAuthentication />, { wrapper });

    return {
      user: userEvent.setup(),
      setting,
      form,
    };
  };

  it('should render collapsed by default and toggle on on click', async () => {
    const { user, form } = renderComponent();

    await user.click(form.getFormTrigger());

    expect(await screen.findByRole('heading', { name: /one time passcode/i, level: 3 })).toBeInTheDocument();
  });

  it('should call mutation and dispatch updateSetting when toggled', async () => {
    const dispatchSpy = vi.spyOn(store, 'updateSetting');

    const { user, form, setting } = renderComponent();

    await user.click(form.getFormTrigger());

    await user.click(form.getSwitch());

    await waitFor(() => {
      expect(dispatchSpy).toHaveBeenCalledWith({ ...setting, mfaEnabled: true });
    });
  });

  it('should show phone number input if MFA is enabled and option is open', async () => {
    const { user, form } = renderComponent();

    await user.click(form.getFormTrigger());

    expect(await screen.findByRole('button', { name: /delete/i })).toBeInTheDocument();
  });
});
