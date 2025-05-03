import { screen, render } from '@testing-library/react';
import SignIn from '../../../src/components/SignIn';
import { AllProviders } from '../../AllProviders';

describe('SignIn', () => {
  it('should render SignIn component', () => {
    render(<SignIn />, { wrapper: AllProviders });
    expect(screen.getByAltText(/landscape/i)).toBeInTheDocument();
  });
});
