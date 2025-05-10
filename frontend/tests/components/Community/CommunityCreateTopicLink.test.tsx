import { screen, render } from '@testing-library/react';
import userEvent from '@testing-library/user-event';

import CommunityCreateTopicLink from '../../../src/components/Community/CommunityCreateTopicLink';
import { AllProviders } from '../../AllProviders';

describe('CommunityCreateTopicLink', () => {
  const renderComponent = () => {
    const linkText = 'Create Topic';

    render(<CommunityCreateTopicLink linkText={linkText} />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      link: screen.getByRole('link'),
    };
  };

  it('should render the provided link text from parent component', () => {
    const { link } = renderComponent();

    expect(link).toHaveTextContent('Create Topic');
  });
});
