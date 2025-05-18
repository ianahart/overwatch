import { screen, render } from '@testing-library/react';

import Tags from '../../../src/components/Tag';
import { AllProviders } from '../../AllProviders';
import { mockUserSearchParams } from '../../setup';

describe('Tags', () => {
  beforeEach(() => {
    mockUserSearchParams({ tag: 'javascript' });
  });

  const renderComponent = () => {
    render(<Tags />, { wrapper: AllProviders });

    return {
      getHeading: () => screen.getByRole('heading', { name: 'Tags under javascript' }),
    };
  };
  it('should render helpful heading with tag description', () => {
    const { getHeading } = renderComponent();

    expect(getHeading()).toBeInTheDocument();
  });

  it('should render the tags list', async () => {
    renderComponent();

    const tags = await screen.findAllByTestId('community-topic-list-item');

    expect(tags.length).toBe(2);
  });
});
