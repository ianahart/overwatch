import { screen, render, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import Description from '../../../../../../src/components/Dashboard/Routes/Admin/Suggestion/Description';
import { AllProviders } from '../../../../../AllProviders';
import * as util from '../../../../../../src/util';

vi.spyOn(util, 'shortenString').mockImplementation((str: string, len: number) => str.slice(0, len) + '...');

describe('Description', () => {
  const getProps = () => {
    const value = 'This is a long test description meant to be shortened when displayed initially.';
    return { value };
  };

  const renderComponent = () => {
    const props = getProps();
    render(<Description {...props} />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      props,
    };
  };

  it('should render the shortened text by default', () => {
    const { props } = renderComponent();

    // shortened text rendered
    const text = screen.getByText(`${props.value.slice(0, 10)}...`);
    expect(text).toBeInTheDocument();
    expect(text).toHaveClass('cursor-pointer');
  });

  it('should open the modal when the text is clicked', async () => {
    const { user, props } = renderComponent();

    const previewText = screen.getByText(`${props.value.slice(0, 10)}...`);
    await user.click(previewText);

    const modal = await screen.findByTestId('DetailsModal');
    expect(modal).toBeInTheDocument();

    // full text visible
    expect(screen.getByText(props.value)).toBeInTheDocument();
  });

  it('should close the modal when the close button is clicked', async () => {
    const { user, props } = renderComponent();

    const previewText = screen.getByText(`${props.value.slice(0, 10)}...`);
    await user.click(previewText);

    const modal = await screen.findByTestId('DetailsModal');
    expect(modal).toBeInTheDocument();

    // close modal
    const closeBtn = await screen.findByTestId('details-modal-close-btn');
    await user.click(closeBtn);

    await waitFor(() => {
      expect(screen.queryByTestId('DetailsModal')).not.toBeInTheDocument();
    });
  });
});
