import { screen, render } from '@testing-library/react';
import TransactionTitle from '../../../../../../src/components/Dashboard/Routes/Admin/Transactions/TransactionTitle';
import { AllProviders } from '../../../../../AllProviders';

describe('TransactionTitle', () => {
  const renderComponent = () => {
    render(<TransactionTitle />, { wrapper: AllProviders });

    return {
      getHeading: () => screen.getByRole('heading', { name: /transactions/i }),
    };
  };

  it('should render the heading', () => {
    const { getHeading } = renderComponent();

    expect(getHeading()).toBeInTheDocument();
  });
});
