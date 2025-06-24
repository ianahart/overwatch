import { screen, render, waitFor } from '@testing-library/react';

import TeamModal from '../../../src/components/Teams/TeamModal';
import { AllProviders } from '../../AllProviders';
import userEvent from '@testing-library/user-event';

describe('TeamModal', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const renderComponent = () => {
    const closeModal = vi.fn();

    render(
      <TeamModal closeModal={closeModal}>
        <p>Test modal content</p>
      </TeamModal>,
      { wrapper: AllProviders }
    );

    return {
      closeModal,
      user: userEvent.setup(),
    };
  };

  it('should render the modal container and children', () => {
    renderComponent();

    expect(screen.getByTestId('team-modal')).toBeInTheDocument();
    expect(screen.getByText('Test modal content')).toBeInTheDocument();
  });

  it('should call "closeModal" when close icon is clicked', async () => {
    const { user, closeModal } = renderComponent();

    await user.click(screen.getByTestId('team-modal-close'));

    await waitFor(() => {
      expect(closeModal).toHaveBeenCalled();
    });
  });
});
