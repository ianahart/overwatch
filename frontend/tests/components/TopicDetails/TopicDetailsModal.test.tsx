import { screen, render, waitFor } from '@testing-library/react';

import TopicDetailsModal from '../../../src/components/TopicDetails/TopicDetailsModal';
import userEvent from '@testing-library/user-event';
import { AllProviders } from '../../AllProviders';

describe('TopicDetailsModal', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = () => {
    return {
      closeModal: vi.fn(),
      content: 'Modal content here',
    };
  };

  const renderComponent = () => {
    const props = getProps();

    render(
      <TopicDetailsModal closeModal={props.closeModal}>
        <p>{props.content}</p>
      </TopicDetailsModal>,
      { wrapper: AllProviders }
    );

    return {
      user: userEvent.setup(),
      getCloseIcon: () => screen.getByTestId('topic-details-modal-close'),
      props,
    };
  };

  it('should render children content', () => {
    const { props } = renderComponent();

    expect(screen.getByText(props.content)).toBeInTheDocument();
  });

  it('should call "closeModal" when the close button is clicked', async () => {
    const { user, props, getCloseIcon } = renderComponent();

    await user.click(getCloseIcon());

    await waitFor(() => {
      expect(props.closeModal).toHaveBeenCalled();
    });
  });
  it('prevents propagation when clicking the close icon container', async () => {
    const { props, user, getCloseIcon } = renderComponent();

    new MouseEvent('click', { bubbles: true });
    const stopPropagation = vi.fn();

    getCloseIcon().addEventListener('click', (e) => (e.stopPropagation = stopPropagation));

    await user.click(getCloseIcon());
    expect(props.closeModal).toHaveBeenCalled();
  });
});
