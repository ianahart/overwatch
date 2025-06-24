import { screen, render } from '@testing-library/react';

import Sidebar from '../../../src/components/Teams/Sidebar';
import { AllProviders } from '../../AllProviders';

describe('Sidebar', () => {
  const renderComponent = () => {
    render(<Sidebar />, { wrapper: AllProviders });

    return {
      getHeading: () => screen.getByRole('heading', { name: /your teams/i }),
    };
  };

  it('should render the heading', () => {
    const { getHeading } = renderComponent();

    expect(getHeading()).toBeInTheDocument();
  });
});
