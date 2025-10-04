import { screen, render } from '@testing-library/react';
import Transaction from '../../../../../../src/components/Dashboard/Routes/Admin/Transactions';
import { AllProviders } from '../../../../../AllProviders';

describe('Transaction', () => {
  const renderComponent = () => {
    render(<Transaction />, { wrapper: AllProviders });

    return {
      getTitleComponent: () => screen.getByTestId('TransactionTitle'),
      getListComponent: () => screen.getByTestId('TransactionList'),
    };
  };

  it('should render the children components', () => {
    const { getTitleComponent, getListComponent } = renderComponent();

    expect(getTitleComponent()).toBeInTheDocument();
    expect(getListComponent()).toBeInTheDocument();
  });
});
