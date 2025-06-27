import { render, waitFor } from '@testing-library/react';
import { useDispatch } from 'react-redux';
import { Mock } from 'vitest';

import Settings from '../../../src/components/Settings';
import { getLoggedInUser } from '../../utils';
import { updateSetting } from '../../../src/state/store';
import { db } from '../../mocks/db';
import { toPlainObject } from 'lodash';
import { server } from '../../mocks/server';
import { HttpResponse, http } from 'msw';
import { baseURL } from '../../../src/util';
import { IFetchSettingsResponse } from '../../../src/interfaces';

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

vi.mock('./Navigation', () => ({
  default: () => <div data-testid="nav" />,
}));

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom');
  return {
    ...actual,
    Outlet: () => <div data-testid="outlet" />,
  };
});

describe('Settings', () => {
  const mockDispatch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const renderComponent = () => {
    const { wrapper } = getLoggedInUser({ settingId: 1 });

    render(<Settings />, { wrapper });
  };

  it('should fetch settings and dispatch "updateSetting"', async () => {
    const updatedSetting = toPlainObject(db.setting.create());

    server.use(
      http.get(`${baseURL}/settings/:settingId`, () => {
        return HttpResponse.json<IFetchSettingsResponse>(
          {
            message: 'success',
            data: updatedSetting,
          },
          { status: 200 }
        );
      })
    );

    renderComponent();
    await waitFor(() => {
      expect(mockDispatch).toHaveBeenCalledWith(updateSetting(updatedSetting));
    });
  });
});
