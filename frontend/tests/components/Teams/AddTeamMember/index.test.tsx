import { screen, render } from '@testing-library/react';

import AddTeamMember from '../../../../src/components/Teams/AddTeamMember';
import { AllProviders } from '../../../AllProviders';

describe('AddTeamMember', () => {
  const renderComponent = () => {
    render(<AddTeamMember />, { wrapper: AllProviders });
    return {
      getHeading: () => screen.getByRole('heading', { level: 3 }),
    };
  };
  it('should render the title', () => {
    const { getHeading } = renderComponent();

    expect(getHeading()).toBeInTheDocument();
  });
});
