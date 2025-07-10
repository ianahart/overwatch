import { screen, render } from '@testing-library/react';

import Suggestion from '../../../../../src/components/Dashboard/Routes/Shared/Suggestion';
import { AllProviders } from '../../../../AllProviders';

describe('Suggestion', () => {
  const renderComponent = () => {
    render(<Suggestion />, { wrapper: AllProviders });

    return {
      getHeading: () => screen.getByRole('heading', { name: /add a suggestion/i }),
    };
  };

  it('should render the main heading to ensure component mounts', () => {
    const { getHeading } = renderComponent();

    expect(getHeading()).toBeInTheDocument();
  });
});
