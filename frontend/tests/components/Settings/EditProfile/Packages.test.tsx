import { screen, render } from '@testing-library/react';

import Packages from '../../../../src/components/Settings/EditProfile/Packages';
import { getLoggedInUser } from '../../../utils';

describe('Packages', () => {
  const renderComponent = () => {
    const { wrapper } = getLoggedInUser(
      {},
      {
        package: {
          basic: {
            price: '0',
            description: 'description of basic',
            items: [
              { id: '1', name: 'Service A', isEditing: 0 },
              { id: '2', name: 'Service B', isEditing: 1 },
            ],
          },
          standard: {
            price: '0',
            description: 'description of standard',
            items: [
              { id: '1', name: 'Service A', isEditing: 0 },
              { id: '2', name: 'Service B', isEditing: 1 },
            ],
          },
          pro: {
            price: '0',
            description: 'description of pro',
            items: [
              { id: '1', name: 'Service A', isEditing: 0 },
              { id: '2', name: 'Service B', isEditing: 1 },
            ],
          },
        },
      }
    );

    render(<Packages />, { wrapper });
  };

  it('should render the heading without crashing, loading proper state', () => {
    renderComponent();

    expect(screen.getByRole('heading', { name: /service & offerings/i })).toBeInTheDocument();
  });
});
