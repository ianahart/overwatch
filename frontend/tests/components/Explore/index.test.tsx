import { screen, render } from '@testing-library/react';
import Explore from '../../../src/components/Explore';
import { AllProviders } from '../../AllProviders';
import userEvent from '@testing-library/user-event';
import { mockNavigate, setMockParams } from '../../setup';

describe('Explore', () => {
  const renderComponent = () => {
    render(<Explore />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      getPaginateButton: () => screen.findByRole('button', { name: /see more/i }),
    };
  };

  it('should render the component correctly', async () => {
    setMockParams({ filter: 'most-relevant' });
    const { getPaginateButton } = renderComponent();

    expect(screen.getByText(/browse reviewers/i)).toBeInTheDocument();
    expect(await getPaginateButton()).toBeInTheDocument();
  });

  it('should navigate when filter changes', async () => {
    setMockParams({ filter: 'most-recent' });

    const { user } = renderComponent();

    const usOnlyOption = screen.getByText(/us only/i);

    await user.click(usOnlyOption);

    expect(mockNavigate).toHaveBeenCalledWith('/explore/domestic');
  });

  it('should fetch and display reviewers correctly', async () => {
    setMockParams({ filter: 'most-recent' });

    renderComponent();

    const reviewers = await screen.findAllByTestId('reviewer-article');

    expect(reviewers.length).toBeGreaterThan(0);
  });

  it('should fetch more reviewers when "See more" is clicked', async () => {
    setMockParams({ filter: 'most-recent' });

    const { user, getPaginateButton } = renderComponent();

    await user.click(await getPaginateButton());

    const reviewers = await screen.findAllByTestId('reviewer-article');

    expect(reviewers.length).toBe(4);
  });
});
