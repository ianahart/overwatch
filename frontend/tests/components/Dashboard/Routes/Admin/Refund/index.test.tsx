import { screen, render } from '@testing-library/react';
import Refund from '../../../../../../src/components/Dashboard/Routes/Admin/Refund';
import { AllProviders } from '../../../../../AllProviders';

describe('Refund', () => {
  const renderComponent = () => {
    render(<Refund />, { wrapper: AllProviders });

    return {
      getTitleComponent: () => screen.getByTestId('RefundTitle'),
      getListComponent: () => screen.getByTestId('RefundList'),
    };
  };

  it('should render children components', () => {
    const { getTitleComponent, getListComponent } = renderComponent();

    expect(getTitleComponent()).toBeInTheDocument();
    expect(getListComponent()).toBeInTheDocument();
  });
});
