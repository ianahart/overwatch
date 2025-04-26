import { render, screen } from '@testing-library/react';
import { vi } from 'vitest'; // or `import { jest } from '@jest/globals';` if using jest
import userEvent from '@testing-library/user-event';
import DevelopmentModePopup from '../../../src/components/Shared/DevelopmentModePopup';

describe('DevelopmentModePopup', () => {
  const renderComponent = () => {
    const handleCloseModal = vi.fn();

    render(<DevelopmentModePopup handleCloseModal={handleCloseModal} />);

    return {
      handleCloseModal,
      closeIcon: screen.getByLabelText(/close/i),
      button: screen.getByRole('button', { name: /got it!/i }),
    };
  };

  it('should call handleCloseModal when clicking the close icon', async () => {
    const { handleCloseModal, closeIcon } = renderComponent();

    await userEvent.click(closeIcon);

    expect(handleCloseModal).toHaveBeenCalled();
  });

  it('should call handleCloseModal when clicking the "Got it!" button', async () => {
    const { handleCloseModal, button } = renderComponent();

    await userEvent.click(button);

    expect(handleCloseModal).toHaveBeenCalled();
  });
});
