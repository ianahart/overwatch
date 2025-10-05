import { screen, render } from '@testing-library/react';
import Suggestion from '../../../../../../src/components/Dashboard/Routes/Admin/Suggestion';
import { AllProviders } from '../../../../../AllProviders';

describe('Suggestion', () => {
  const renderComponent = () => {
    render(<Suggestion />, { wrapper: AllProviders });

    return {
      getHeaderComponent: () => screen.getByTestId('SuggestionHeader'),
      getListComponent: () => screen.getByTestId('SuggestionList'),
    };
  };

  it('should render the children components', () => {
    const { getListComponent, getHeaderComponent } = renderComponent();

    expect(getListComponent()).toBeInTheDocument();
    expect(getHeaderComponent()).toBeInTheDocument();
  });
});
