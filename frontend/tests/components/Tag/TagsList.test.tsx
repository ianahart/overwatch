import { screen, render } from '@testing-library/react';
import userEvent from '@testing-library/user-event';

import TagsList from '../../../src/components/Tag/TagsList';
import { AllProviders } from '../../AllProviders';

describe('TagsList', () => {
  const renderComponent = () => {
    const query = 'javascript';

    render(<TagsList query={query} />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      getNextButton: () => screen.findByRole('button', { name: /next/i }),
    };
  };
  it('should fetch and display topics on mount', async () => {
    const { getNextButton } = renderComponent();

    expect(await getNextButton()).toBeInTheDocument();
    expect(await screen.findByText('1 of 10')).toBeInTheDocument();
  });

  it('should display the correct number of topics', async () => {
    renderComponent();

    const topics = await screen.findAllByTestId('community-topic-list-item');

    expect(topics.length).toBe(2);
  });

  it('should navigate to the next page when "Next" is clicked', async () => {
    const { user, getNextButton } = renderComponent();

    const nextButton = await getNextButton();

    await user.click(nextButton);

    expect(await screen.findByText('2 of 10')).toBeInTheDocument();
  });

  it('should navigate to the prev page when "Prev" is clicked', async () => {
    const { user, getNextButton } = renderComponent();

    const nextButton = await getNextButton();

    await user.click(nextButton);
    await user.click(nextButton);

    const prevButton = await screen.findByRole('button', { name: /prev/i });

    await user.click(prevButton);

    expect(await screen.findByText('2 of 10')).toBeInTheDocument();
  });
});
