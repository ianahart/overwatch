import { screen, render } from '@testing-library/react';

import ContactInfo from '../../../../src/components/Settings/ContactInfo';
import { AllProviders } from '../../../AllProviders';

describe('ContactInfo', () => {
  const renderComponent = () => {
    render(<ContactInfo />, { wrapper: AllProviders });
  };

  it('should render the heading proving the component mounts', () => {
    renderComponent();

    expect(screen.getByRole('heading', { name: /contact info/i })).toBeInTheDocument();
  });
});
