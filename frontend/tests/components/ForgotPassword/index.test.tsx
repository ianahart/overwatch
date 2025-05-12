import { screen, render } from '@testing-library/react';
import ForgotPassword from '../../../src/components/ForgotPassword/';
import { AllProviders } from '../../AllProviders';

describe('ForgotPassword', () => {
  const renderComponent = () => {
    render(<ForgotPassword />, { wrapper: AllProviders });

    return {
      container: screen.getByTestId('forgot-password-container'),
    };
  };

  it('should render the ForgotPassword component', () => {
    const { container } = renderComponent();

    expect(container).toBeInTheDocument();
  });
});
