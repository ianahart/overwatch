import { screen, render } from '@testing-library/react';
import RefundTitle from '../../../../../../src/components/Dashboard/Routes/Admin/Refund/RefundTitle';
import { AllProviders } from '../../../../../AllProviders';

describe('RefundTitle', () => {
  const renderComponent = () => {
    render(<RefundTitle />, { wrapper: AllProviders });

    return {
      getHeading: () => screen.getByRole('heading', { name: /refund requests/i }),
    };
  };

  it('should render the heading', () => {
    const { getHeading } = renderComponent();

    expect(getHeading()).toBeInTheDocument();
  });
});
