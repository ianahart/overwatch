import { screen, render, waitFor } from '@testing-library/react';

import Profile from '../../../src/components/Profile';
import { AllProviders } from '../../AllProviders';
import { server } from '../../mocks/server';
import { HttpResponse, http } from 'msw';
import { baseURL } from '../../../src/util';
import { createUserAndProfile } from '../../mocks/dbActions';

vi.mock('../../util', () => ({
  retrieveTokens: () => ({ token: 'mock-token' }),
}));

describe('Profile', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = (overrides = {}) => {
    return {
      profileId: 1,
      ...overrides,
    };
  };

  const renderComponent = () => {
    const props = getProps();

    render(<Profile {...props} />, { wrapper: AllProviders });

    return { props };
  };

  it('should render ReviewerProfile when role is "REVIEWER"', async () => {
    renderComponent();

    await waitFor(() => {
      expect(screen.getByRole('heading', { level: 3, name: /skills/i })).toBeInTheDocument();
    });
  });

  it('should render the UserProfile when the role is "USER"', async () => {
    server.use(
      http.get(`${baseURL}/profiles/:profileId`, () => {
        const data = createUserAndProfile({ role: 'USER', userId: 2 });

        return HttpResponse.json(data, { status: 200 });
      })
    );

    renderComponent();

    await waitFor(() => {
      expect(screen.queryByRole('heading', { level: 3, name: /skills/i })).not.toBeInTheDocument();
    });
  });
});
