import { screen, render } from '@testing-library/react';
import BadgeHeader from '../../../../../../src/components/Dashboard/Routes/Admin/Badge/BadgeHeader';
import { AllProviders } from '../../../../../AllProviders';

describe('CreateBadge', () => {
  const renderComponent = () => {
    render(<BadgeHeader />, { wrapper: AllProviders });

    return {
      getHeading: () => screen.getByRole('heading', { name: /currently available badges/i }),
    };
  };

  it('should render the heading', () => {
    const { getHeading } = renderComponent();
    expect(getHeading()).toBeInTheDocument();
  });
});
