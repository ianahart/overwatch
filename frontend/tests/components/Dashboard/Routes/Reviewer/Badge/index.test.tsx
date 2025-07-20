import { screen, render } from '@testing-library/react';

import ReviewerBadge from '../../../../../../src/components/Dashboard/Routes/Reviewer/Badge';
import { AllProviders } from '../../../../../AllProviders';

describe('ReviewerBadge', () => {
  const getComponents = () => {
    return {
      getBadgeHeaderComponent: () => screen.getByTestId('BadgeHeader'),
      getBadgeListComponent: () => screen.getByTestId('BadgeList'),
    };
  };

  const renderComponent = () => {
    render(<ReviewerBadge />, { wrapper: AllProviders });

    return {
      components: getComponents(),
    };
  };

  it('should render BadgeHeader and BadgeList components without crashing', () => {
    const { components } = renderComponent();

    const { getBadgeListComponent, getBadgeHeaderComponent } = components;

    expect(getBadgeHeaderComponent()).toBeInTheDocument();
    expect(getBadgeListComponent()).toBeInTheDocument();
  });
});
