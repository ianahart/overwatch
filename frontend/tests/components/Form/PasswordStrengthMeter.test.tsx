import { screen, render } from '@testing-library/react';

import PasswordStrengthMeter from '../../../src/components/Form/PasswordStrengthMeter';

describe('PasswordStrengthMeter', () => {
  const renderComponent = (password: string = '') => {
    render(<PasswordStrengthMeter password={password} />);

    return {
      strengthText: () => screen.queryByText(/very weak|weak|good|very good/i),
      strengthMeter: () => screen.queryByRole('presentation') as HTMLElement,
    };
  };

  it('should render with no strength when initially rendered', () => {
    const { strengthText, strengthMeter } = renderComponent();

    expect(strengthText()).not.toBeInTheDocument();
    expect(strengthMeter()).not.toHaveClass('bg-red-400');
    expect(strengthMeter()).not.toHaveClass('bg-yellow-400');
    expect(strengthMeter()).not.toHaveClass('bg-blue-400');
    expect(strengthMeter()).not.toHaveClass('bg-green-400');
  });

  it('should display "Very Weak" strength with red color when only lowercase letters', () => {
    const { strengthText, strengthMeter } = renderComponent('test');

    expect(strengthText()).toHaveTextContent('Very Weak');
    expect(strengthMeter()).toHaveClass('bg-red-400');
  });

  it('should display "Weak" strength with yellow color when only lowercase and uppercase letters', () => {
    const { strengthText, strengthMeter } = renderComponent('Test');

    expect(strengthText()).toHaveTextContent('Weak');
    expect(strengthMeter()).toHaveClass('bg-yellow-400');
  });

  it('should display "Good" strength when three out of four: letters, uppercase, special chars, numbers are given', () => {
    const { strengthText, strengthMeter } = renderComponent('Test12345');

    expect(strengthText()).toHaveTextContent('Good');
    expect(strengthMeter()).toHaveClass('bg-blue-400');
  });

  it('should display "Very Good" strength when four out of four: letters, uppercase, special chars, numbers are given', () => {
    const { strengthText, strengthMeter } = renderComponent('Test12345%');

    expect(strengthText()).toHaveTextContent('Very Good');
    expect(strengthMeter()).toHaveClass('bg-green-400');
  });
});
