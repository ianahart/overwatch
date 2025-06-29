import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';
import { useDispatch } from 'react-redux';
import { Mock } from 'vitest';
import userEvent from '@testing-library/user-event';
import { HttpResponse, http } from 'msw';
import { server } from '../../../mocks/server';

import NotificationSwitch from '../../../../src/components/Settings/Notification/NotificationSwitch';
import { getLoggedInUser } from '../../../utils';
import { db } from '../../../mocks/db';
import { ISetting, IUpdateSettingResponse } from '../../../../src/interfaces';
import { updateSetting } from '../../../../src/state/store';
import { baseURL } from '../../../../src/util';

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

describe('NotificationSwitch', () => {
  const mockDispatch = vi.fn();
  const setting: ISetting = { ...toPlainObject(db.setting.create()) };

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const getProps = (overrides = {}) => {
    return {
      setting,
      value: true,
      propName: 'emailOn',
      ...overrides,
    };
  };

  const renderComponent = (overrides = {}) => {
    const props = getProps(overrides);

    const { wrapper } = getLoggedInUser(
      {},
      {
        setting: {
          setting: props.setting,
        },
      }
    );

    render(<NotificationSwitch {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      props,
    };
  };

  it('should render switch and correct notification label', () => {
    renderComponent({ propName: 'reviewInProgressNotifOn', value: true });

    expect(screen.getByText(/review inprogress notifications/i)).toBeInTheDocument();
    expect(
      screen.getByText(/notifications about your code reviews that are currently in progress/i)
    ).toBeInTheDocument();
  });

  it('should toggle off and dispatch updateSetting with updated value', async () => {
    server.use(
      http.put(`${baseURL}/settings/:settingId`, () => {
        return HttpResponse.json<IUpdateSettingResponse>(
          {
            message: 'success',
            data: { ...setting, reviewInProgressNotifOn: false },
          },
          { status: 200 }
        );
      })
    );

    const { user } = renderComponent({ propName: 'reviewInProgressNotifOn', value: true });

    const checkbox = screen.getByRole('checkbox');

    await user.click(checkbox);

    await waitFor(() => {
      expect(mockDispatch).toHaveBeenCalledWith(updateSetting({ ...setting, reviewInProgressNotifOn: false }));
    });
  });

  it('should toggle on and dispatch updateSetting with updated value', async () => {
    server.use(
      http.put(`${baseURL}/settings/:settingId`, async () =>
        HttpResponse.json<IUpdateSettingResponse>({
          message: 'success',
          data: {
            ...setting,
            reviewInCompleteNotifOn: true,
          },
        })
      )
    );

    const { user } = renderComponent({ propName: 'reviewInCompleteNotifOn', value: false });

    const checkbox = screen.getByRole('checkbox');

    await user.click(checkbox);

    await waitFor(() => {
      expect(mockDispatch).toHaveBeenCalledWith(updateSetting({ ...setting, reviewInCompleteNotifOn: true }));
    });
  });
});
