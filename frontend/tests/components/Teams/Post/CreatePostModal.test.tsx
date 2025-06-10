import { screen, render, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';

import CreatePostModal from '../../../../src/components/Teams/Post/CreatePostModal';

describe('CreatePostModal', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = () => {
    return {
      closeModal: vi.fn(),
    };
  };

  const renderComponent = () => {
    const props = getProps();
    render(
      <CreatePostModal {...props}>
        <div>Modal Content</div>
      </CreatePostModal>
    );

    return {
      user: userEvent.setup(),
      props,
    };
  };

  it('should render children', () => {
    renderComponent();

    expect(screen.getByText(/modal content/i)).toBeInTheDocument();
  });

  it('should call "closeModal" when close button is clicked', async () => {
    const { user, props } = renderComponent();

    await user.click(screen.getByTestId('post-modal-close-btn'));

    await waitFor(() => {
      expect(props.closeModal).toHaveBeenCalled();
    });
  });
});
