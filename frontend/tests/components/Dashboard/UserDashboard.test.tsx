import { screen, render } from '@testing-library/react';

import UserDashboard from '../../../src/components/Dashboard/UserDashboard';
import { AllProviders } from '../../AllProviders';

describe('UserDashboard', () => {
  const getComponents = () => {
    return {
      getSidebar: () => screen.getByTestId('UserSidebarNavigation'),
      getOutlet: () => screen.getByTestId('OutletContainer'),
    };
  };

  const renderComponent = () => {
    render(<UserDashboard />, { wrapper: AllProviders });

    return {
      components: getComponents(),
    };
  };

  it('should render without crashing', () => {
    const { components } = renderComponent();

    expect(components.getOutlet()).toBeInTheDocument();
    expect(components.getSidebar()).toBeInTheDocument();
  });
});
