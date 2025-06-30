import { screen, render } from '@testing-library/react';

import EditProfile from '../../../../src/components/Settings/EditProfile';
import { AllProviders } from '../../../AllProviders';

describe('EditProfile', () => {
  const renderComponent = () => {
    render(<EditProfile />, { wrapper: AllProviders });
  };

  it('should render the heading to make sure the main component mounts', () => {
    renderComponent();
    expect(screen.getByRole('heading', { name: /edit profile/i })).toBeInTheDocument();
  });
});
