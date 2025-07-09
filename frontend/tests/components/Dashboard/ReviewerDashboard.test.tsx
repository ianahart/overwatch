import { screen, render } from '@testing-library/react';

import ReviewerDashboard from '../../../src/components/Dashboard/ReviewerDashboard';
import { AllProviders } from '../../AllProviders';

describe('ReviewerDashboard', () => {
  const getComponents = () => {
    return {
      getSidebar: () => screen.getByTestId('ReviewerSidebarNavigation'),
      getOutlet: () => screen.getByTestId('OutletContainer'),
    };
  };

  const renderComponent = () => {
    render(<ReviewerDashboard />, { wrapper: AllProviders });

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
