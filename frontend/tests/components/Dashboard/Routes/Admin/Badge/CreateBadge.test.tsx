import { screen, render } from '@testing-library/react';
import CreateBadge from '../../../../../../src/components/Dashboard/Routes/Admin/Badge/CreateBadge';
import { AllProviders } from '../../../../../AllProviders';

describe('CreateBadge', () => {
  const renderComponent = () => {
    render(<CreateBadge />, { wrapper: AllProviders });

    return {
      getHeading: () => screen.getByRole('heading', { name: /create a badge/i }),
    };
  };

  it('should render the heading', () => {
    const { getHeading } = renderComponent();
    expect(getHeading()).toBeInTheDocument();
  });
});
