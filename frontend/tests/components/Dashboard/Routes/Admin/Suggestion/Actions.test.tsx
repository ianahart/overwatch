import { screen, render, waitFor } from '@testing-library/react';
import { getLoggedInUser } from '../../../../../utils';
import Actions from '../../../../../../src/components/Dashboard/Routes/Admin/Suggestion/Actions';
import userEvent from '@testing-library/user-event';
import { capitalize } from 'lodash';

describe('Actions', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = () => {
    return {
      value: 'PENDING',
      id: '1',
      handleUpdateSuggestion: vi.fn(),
      handleDeleteSuggestion: vi.fn(),
    };
  };

  const renderComponent = () => {
    const props = getProps();

    const { wrapper } = getLoggedInUser();

    render(<Actions {...props} />, { wrapper });

    return {
      props,
      user: userEvent.setup(),
      getUpdateBtn: () => screen.getByRole('button', { name: /update/i }),
      getDeleteBtn: () => screen.getByRole('button', { name: /delete/i }),
    };
  };

  it('should render the initial feedback status', () => {
    const { props, getUpdateBtn, getDeleteBtn } = renderComponent();

    expect(screen.getByText(capitalize(props.value))).toBeInTheDocument();
    expect(getUpdateBtn()).toBeInTheDocument();
    expect(getDeleteBtn()).toBeInTheDocument();
  });

  it('should open the modal when "Update" button is clicked', async () => {
    const { user, props, getUpdateBtn } = renderComponent();

    await user.click(getUpdateBtn());

    expect(await screen.findByTestId('DetailsModal')).toBeInTheDocument();
    expect(screen.getByText(/you currently have this suggestion/i)).toBeInTheDocument();
    expect(screen.getByRole('combobox')).toHaveValue(props.value);
  });

  it('should update suggestion when a new status is selected', async () => {
    const { user, props, getUpdateBtn } = renderComponent();

    await user.click(getUpdateBtn());
    const select = await screen.findByRole('combobox');

    await user.selectOptions(select, 'IMPLEMENTED');
    await waitFor(() => {
      expect(props.handleUpdateSuggestion).toHaveBeenCalledWith('IMPLEMENTED', '1');
    });
  });

  it('should delete suggestion when delete button is clicked', async () => {
    const { user, props, getDeleteBtn } = renderComponent();

    await user.click(getDeleteBtn());

    await waitFor(() => {
      expect(props.handleDeleteSuggestion).toHaveBeenCalledWith('1');
    });
  });

  it('should close modal when the close button is clicked', async () => {
    const { user, getUpdateBtn } = renderComponent();

    await user.click(getUpdateBtn());

    const modal = await screen.findByTestId('DetailsModal');
    expect(modal).toBeInTheDocument();

    await user.click(await screen.findByTestId('details-modal-close-btn'));
    await waitFor(() => {
      expect(screen.queryByTestId('DetailsModal')).not.toBeInTheDocument();
    });
  });
});
