import { screen, render, waitFor } from '@testing-library/react';
import DetailsModal from '../../../../../src/components/Dashboard/Routes/Admin/DetailsModal';
import { AllProviders } from '../../../../AllProviders';
import userEvent from '@testing-library/user-event';

describe('DetailsModal', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = () => {
    const handleCloseModal = vi.fn();
    const children = (
      <div>
        <p>modal content</p>
      </div>
    );
    return { handleCloseModal, children };
  };

  const renderComponent = () => {
    const props = getProps();

    render(<DetailsModal {...props} />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      getCloseBtn: () => screen.getByTestId('details-modal-close-btn'),
      props,
    };
  };

  it('should render children inside the modal', () => {
    renderComponent();

    expect(screen.getByText('modal content')).toBeInTheDocument();
  });

  it('should render the close button', () => {
    const { getCloseBtn } = renderComponent();

    expect(getCloseBtn()).toBeInTheDocument();
  });

  it('should call "handleCloseModal" when the close button is clicked', async () => {
    const { user, getCloseBtn, props } = renderComponent();

    await user.click(getCloseBtn());

    await waitFor(() => {
      expect(props.handleCloseModal).toHaveBeenCalled();
    });
  });

  it('should apply the modal wrapper classes', () => {
    renderComponent();

    const modalWrapper = screen.getByText('modal content').closest('div')?.parentElement;
    expect(modalWrapper?.className).toContain('bg-gray-900');
  });
});
