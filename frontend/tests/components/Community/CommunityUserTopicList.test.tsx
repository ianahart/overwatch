import { screen, render } from '@testing-library/react';
import CommunityUserTopicList from '../../../src/components/Community/CommunityUserTopicList';
import userEvent from '@testing-library/user-event';
import { AllProviders } from '../../AllProviders';
import { db } from '../../mocks/db';

describe('CommunityUserTopicList', () => {
  const renderComponent = (overrides = {}) => {
    const user = db.user.create();
    const token = db.token.create();

    const props = { userId: user.id, token: token.token, ...overrides };

    render(<CommunityUserTopicList {...props} />, { wrapper: AllProviders });

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

    const topics = await screen.findAllByRole('link');

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
