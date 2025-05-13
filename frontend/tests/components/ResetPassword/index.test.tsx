import { screen, render } from '@testing-library/react';
import ResetPassword from '../../../src/components/ResetPassword/';
import { AllProviders } from '../../AllProviders';

describe('ResetPassword', () => {
  const renderComponent = () => {
    render(<ResetPassword />, { wrapper: AllProviders });

    return {
      container: screen.getByTestId('reset-password-container'),
    };
  };

  it('should render the ResetPassword component', () => {
    const { container } = renderComponent();

    expect(container).toBeInTheDocument();
  });
});
