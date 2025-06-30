import { screen, render } from '@testing-library/react';

import GetPaid from '../../../../src/components/Settings/GetPaid';
import { AllProviders } from '../../../AllProviders';

describe('GetPaid', () => {
  const renderComponent = () => {
    render(<GetPaid />, { wrapper: AllProviders });
  };

  it('should render helper text to verify that component has mounted', () => {
    renderComponent();

    expect(screen.getByText(/click the get paid button next to a review you have completed/i)).toBeInTheDocument();
  });
});
