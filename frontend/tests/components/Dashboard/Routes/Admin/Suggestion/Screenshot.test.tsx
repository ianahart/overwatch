import { screen, render, waitFor } from '@testing-library/react';
import Screenshot from '../../../../../../src/components/Dashboard/Routes/Admin/Suggestion/Screenshot';
import { AllProviders } from '../../../../../AllProviders';
import userEvent from '@testing-library/user-event';

describe('Screenshot', () => {
  const getProps = () => {
    const value = 'https://example.com/image.png';

    return { value };
  };

  const renderComponent = () => {
    const props = getProps();
    render(<Screenshot {...props} />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      props,
    };
  };

  it('should render the thumbnail image by default', () => {
    const { props } = renderComponent();

    const image = screen.getByAltText('screenshot');
    expect(image).toBeInTheDocument();
    expect(image).toHaveAttribute('src', props.value);
  });

  it('should open and close the modal when image is clicked', async () => {
    const { user } = renderComponent();
    const image = screen.getByAltText('screenshot');

    await user.click(image);

    const modal = await screen.findByTestId('DetailsModal');
    expect(modal).toBeInTheDocument();
  });

  it('should close the modal when the close button is clicked', async () => {
    const { user } = renderComponent();

    const image = screen.getByAltText('screenshot');
    await user.click(image);

    const modal = await screen.findByTestId('DetailsModal');
    expect(modal).toBeInTheDocument();

    await user.click(await screen.findByTestId('details-modal-close-btn'));
    await waitFor(() => {
      expect(screen.queryByTestId('DetailsModal')).not.toBeInTheDocument();
    });
  });
});
