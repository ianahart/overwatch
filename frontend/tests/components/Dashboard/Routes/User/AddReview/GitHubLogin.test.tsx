import { screen, render } from '@testing-library/react';

import GitHubLogin from '../../../../../../src/components/Dashboard/Routes/User/AddReview/GitHubLogin';
import { AllProviders } from '../../../../../AllProviders';
import userEvent from '@testing-library/user-event';

describe('GitHubLogin', () => {
  beforeEach(() => {
    vi.stubGlobal('location', {
      ...window.location,
      assign: vi.fn(),
    });
    import.meta.env.VITE_GITHUB_CLIENT_ID = 'mock-client-id';
  });

  afterEach(() => {
    vi.unstubAllGlobals();
  });

  const renderComponent = () => {
    render(<GitHubLogin />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      getGitHubBtn: () => screen.getByRole('button', { name: /sign in with github/i }),
    };
  };

  it('should render GitHub login message and button', () => {
    const { getGitHubBtn } = renderComponent();

    expect(screen.getByText(/please sign in with github/i)).toBeInTheDocument();
    expect(getGitHubBtn()).toBeInTheDocument();
  });

  it('should redirect to GitHub OAuth when button is clicked', async () => {
    const expectedUrl = `https://github.com/login/oauth/authorize?client_id=mock-client-id`;

    const { user, getGitHubBtn } = renderComponent();

    await user.click(getGitHubBtn());

    expect(window.location.assign).toHaveBeenCalledWith(expectedUrl);
  });
});
