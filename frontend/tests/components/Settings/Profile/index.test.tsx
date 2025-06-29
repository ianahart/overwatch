import { screen, render } from '@testing-library/react';
import { getLoggedInUser } from '../../../utils';
import ProfileSettings from '../../../../src/components/Settings/Profile';

describe('ProfileSettings', () => {
  const renderComponent = () => {
    const { wrapper } = getLoggedInUser({ role: 'REVIEWER' });

    render(<ProfileSettings />, { wrapper });
  };

  it("should render profile visibility if user's role is REVIEWER", async () => {
    renderComponent();

    expect(await screen.findByTestId('profile-visibility-component')).toBeInTheDocument();
  });
});
