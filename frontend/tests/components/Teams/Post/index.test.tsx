import { screen, render, waitFor } from '@testing-library/react';

import Post from '../../../../src/components/Teams/Post';
import { AllProviders } from '../../../AllProviders';
import userEvent from '@testing-library/user-event';

describe('Post', () => {
  const renderComponent = () => {
    render(<Post />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      getButton: () => screen.getByRole('button', { name: /create post/i }),
    };
  };

  it('should render create post button', () => {
    const { getButton } = renderComponent();

    expect(getButton()).toBeInTheDocument();
  });

  it('should open modal when Create post button is clicked', async () => {
    const { user, getButton } = renderComponent();

    await user.click(getButton());

    expect(await screen.findByTestId('create-post-modal')).toBeInTheDocument();
  });

  it('should close modal when close modal button is clicked', async () => {
    const { user, getButton } = renderComponent();

    await user.click(getButton());
    expect(await screen.findByTestId('create-post-modal')).toBeInTheDocument();

    await user.click(await screen.findByTestId('post-modal-close-btn'));

    await waitFor(() => {
      expect(screen.queryByTestId('create-post-modal')).not.toBeInTheDocument();
    });
  });
});
