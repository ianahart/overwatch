import { screen, render } from '@testing-library/react';

import CommunityHeader from '../../../src/components/Community/CommunityHeader';
import { AllProviders } from '../../AllProviders';

describe('CommunityHeader', () => {
  const renderComponent = () => {
    const heading = 'test heading';
    const description = 'test description';

    render(<CommunityHeader heading={heading} description={description} />, { wrapper: AllProviders });
  };

  it('should render the passed down heading and description props', () => {
    renderComponent();

    expect(screen.getByText('test heading')).toBeInTheDocument();
    expect(screen.getByText('test description')).toBeInTheDocument();
  });
});
