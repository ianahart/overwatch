import { screen, render, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';

import ProfileVisibility from '../../../../src/components/Settings/Profile/ProfileVisibility';
import { getLoggedInUser } from '../../../utils';
import { server } from '../../../mocks/server';
import { HttpResponse, http } from 'msw';
import { baseURL } from '../../../../src/util';

describe('ProfileVisibility', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getUserActions = () => {
    return {
      getSwitch: () => screen.getByTestId('settings-switch'),
    };
  };

  const renderComponent = () => {
    const { wrapper } = getLoggedInUser();

    render(<ProfileVisibility />, { wrapper });

    return {
      user: userEvent.setup(),
      actions: getUserActions(),
    };
  };

  it('should render component and show toggle label', () => {
    renderComponent();

    expect(screen.getByRole('heading', { name: /profile visibility/i, level: 3 })).toBeInTheDocument();
    expect(screen.getByText(/toggling this on and off/i)).toBeInTheDocument();
  });

  it('should toggle visibility off and update state', async () => {
    let isVisible = true;

    server.use(
      http.get(`${baseURL}/profiles/:profileId/visibility`, () => {
        return HttpResponse.json({ message: 'success', data: isVisible });
      }),
      http.patch(`${baseURL}/profiles/:profileId/visibility`, () => {
        isVisible = !isVisible;
        return HttpResponse.json({ message: 'success', data: isVisible });
      })
    );

    const { user, actions } = renderComponent();

    expect(await screen.findByTestId('settings-switch')).toBeChecked();

    await user.click(actions.getSwitch());

    await waitFor(() => {
      expect(actions.getSwitch()).not.toBeChecked();
    });
  });
});
