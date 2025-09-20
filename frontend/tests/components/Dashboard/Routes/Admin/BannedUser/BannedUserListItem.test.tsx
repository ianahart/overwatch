import { screen, render, waitFor } from '@testing-library/react';
import { createBannedUsers } from '../../../../../mocks/dbActions';
import { getLoggedInUser } from '../../../../../utils';
import BannedUserListItem from '../../../../../../src/components/Dashboard/Routes/Admin/BannedUser/BannedUserListItem';
import userEvent from '@testing-library/user-event';
import dayjs from 'dayjs';

describe('BannedUserListItem', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = () => {
    const [user] = createBannedUsers(1);

    return {
      user,
      handleSetView: vi.fn(),
      updateBannedUserState: vi.fn(),
      removeBannedUserState: vi.fn(),
    };
  };

  const getElements = () => {
    return {
      getUnBanBtn: () => screen.getByRole('button', { name: /unban/i }),
      getDetailsBtn: () => screen.getByRole('button', { name: /details/i }),
      getEditBtn: () => screen.getByRole('button', { name: /edit/i }),
    };
  };

  const renderComponent = () => {
    const props = getProps();

    const { wrapper } = getLoggedInUser();

    render(<BannedUserListItem {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      props,
      elements: getElements(),
    };
  };

  it('should render user information correctly', () => {
    const { props } = renderComponent();

    const { fullName, email, createdAt, banDate, time } = props.user;

    expect(screen.getByText(fullName)).toBeInTheDocument();
    expect(screen.getByText(email)).toBeInTheDocument();
    expect(screen.getByText(dayjs(createdAt).format('MM/DD/YYYY'))).toBeInTheDocument();
    expect(screen.getByText(dayjs(banDate).format('MM/DD/YYYY'))).toBeInTheDocument();
    expect(screen.getByText(`${time / 86400} days`)).toBeInTheDocument();
  });

  it('should call the delete mutation and remove user on unban', async () => {
    const { user, elements, props } = renderComponent();

    await user.click(elements.getUnBanBtn());

    await waitFor(() => {
      expect(props.removeBannedUserState).toHaveBeenCalledWith(props.user.id);
    });
  });

  it('should open and close details modal', async () => {
    const { elements, user } = renderComponent();

    await user.click(elements.getDetailsBtn());

    expect(await screen.findByTestId('DetailsModal'));

    await user.click(screen.getByTestId('details-modal-close-btn'));

    await waitFor(() => {
      expect(screen.queryByTestId('DetailsModal')).not.toBeInTheDocument();
    });
  });

  it('should open and close edit modal', async () => {
    const { elements, user } = renderComponent();

    await user.click(elements.getEditBtn());

    expect(await screen.findByTestId('BannedUserForm')).toBeInTheDocument();

    await user.click(screen.getByTestId('details-modal-close-btn'));

    await waitFor(() => {
      expect(screen.queryByTestId('BannedUserForm')).not.toBeInTheDocument();
    });
  });
});
