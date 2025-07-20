import { screen, render } from '@testing-library/react';

import BadgeHeader from '../../../../../../src/components/Dashboard/Routes/Reviewer/Badge/BadgeHeader';
import { AllProviders } from '../../../../../AllProviders';

describe('BadgeHeader', () => {
  const renderComponent = () => {
    render(<BadgeHeader />, { wrapper: AllProviders });

    return {
      getHeading: () => screen.getByRole('heading', { name: /badges/i }),
      getText: () => screen.getByText(/where you can find badges/i),
    };
  };

  it('should render the badge header and helper text', () => {
    const { getHeading, getText } = renderComponent();

    expect(getHeading()).toBeInTheDocument();
    expect(getText()).toBeInTheDocument();
  });
});
