import { screen, render } from '@testing-library/react';
import SuggestionHeader from '../../../../../../src/components/Dashboard/Routes/Admin/Suggestion/SuggestionHeader';
import { AllProviders } from '../../../../../AllProviders';

describe('SuggestionHeader', () => {
  const renderComponent = () => {
    render(<SuggestionHeader />, { wrapper: AllProviders });

    return {
      getHeading: () => screen.getByRole('heading', { level: 2, name: /suggestions/i }),
    };
  };

  it('should render the heading', () => {
    const { getHeading } = renderComponent();

    expect(getHeading()).toBeInTheDocument();
  });
});
