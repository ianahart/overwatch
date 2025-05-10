import { screen, render } from '@testing-library/react';

import CommunityTopicList from '../../../src/components/Community/CommunityTopicList';
import { AllProviders } from '../../AllProviders';
import userEvent from '@testing-library/user-event';

describe('CommunityTopicList', () => {
  const renderComponent = () => {
    render(<CommunityTopicList />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      getNextButton: () => screen.findByRole('button', { name: /next/i }),
      getPrevButton: () => screen.queryByRole('button', { name: /prev/i }),
    };
  };

  it('should render with correct initial state', async () => {
    renderComponent();

    expect(await screen.findByText('1 of 2')).toBeInTheDocument();
  });

  it('should fetch and display topics on render', async () => {
    renderComponent();

    const topics = await screen.findAllByTestId('community-topic-list-item');

    expect(topics).toHaveLength(10);
  });

  it('should navigate to next page on "Next" button click', async () => {
    const { user, getNextButton } = renderComponent();

    const nextButton = await getNextButton();

    await user.click(nextButton);

    expect(await screen.findByText('2 of 2')).toBeInTheDocument();
  });

  it('should navigate to previous page on "Prev" button click', async () => {
    const { user, getNextButton, getPrevButton } = renderComponent();

    const nextButton = await getNextButton();
    const prevButton = getPrevButton();

    await user.click(nextButton);

    if (prevButton !== null) {
      user.click(prevButton);
      expect(await screen.findByText('1 of 2')).toBeInTheDocument();
    }
  });
});
