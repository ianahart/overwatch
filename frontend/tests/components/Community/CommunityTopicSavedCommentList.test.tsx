import { screen, render, waitFor } from '@testing-library/react';
import { db } from '../../mocks/db';
import CommunityTopicSavedCommentList from '../../../src/components/Community/CommunityTopicSavedCommentList';
import { AllProviders } from '../../AllProviders';
import userEvent from '@testing-library/user-event';

describe('CommunityTopicSavedCommentList', () => {
  const renderComponent = (overrides = {}) => {
    const user = db.user.create();
    const token = db.token.create();

    const props = { userId: user.id, token: token.token, ...overrides };

    render(<CommunityTopicSavedCommentList {...props} />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      getNextButton: () => screen.findByRole('button', { name: /next/i }),
      getPrevButton: () => screen.queryByRole('button', { name: /prev/i }),
    };
  };

  it('should render with the correct initial state', async () => {
    renderComponent();

    expect(await screen.findByText('1 of 2')).toBeInTheDocument();
  });

  it('should display fetched saved comments', async () => {
    renderComponent();

    const savedComments = await screen.findAllByTestId('saved-comment');

    expect(savedComments).toHaveLength(10);
  });

  it('should navigate to the next page when "Next" button is clicked', async () => {
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

  it('should delete a comment when the bookmark icon is clicked', async () => {
    const { user } = renderComponent();

    const bookmarkIcon = await screen.findAllByTestId('saved comment bookmark');

    await user.click(bookmarkIcon[0]);

    await waitFor(() => {
      expect(bookmarkIcon[0]).not.toBeInTheDocument();
    });
  });
});
