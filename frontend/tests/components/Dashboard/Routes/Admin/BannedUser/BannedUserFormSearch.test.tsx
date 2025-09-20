import { screen, render, waitFor, fireEvent } from '@testing-library/react';
import { getLoggedInUser } from '../../../../../utils';
import BannedUserFormSearch from '../../../../../../src/components/Dashboard/Routes/Admin/BannedUser/BannedUserFormSearch';
import userEvent from '@testing-library/user-event';

describe('BannedUserFormSearch', () => {
  beforeEach(() => {
    vi.useRealTimers();
  });

  afterEach(() => {
    vi.clearAllMocks();
  });

  const getElements = () => {
    return {
      getInput: () => screen.getByLabelText(/select a user/i),
    };
  };

  const renderComponent = () => {
    const handleSetSelectedUser = vi.fn();
    const { wrapper } = getLoggedInUser();

    render(<BannedUserFormSearch handleSetSelectedUser={handleSetSelectedUser} />, { wrapper });
    return {
      handleSetSelectedUser,
      user: userEvent.setup(),
      elements: getElements(),
    };
  };

  it('should render input with label', () => {
    const { elements } = renderComponent();

    expect(elements.getInput()).toBeInTheDocument();
  });

  it('should search and display results', async () => {
    const { elements, user } = renderComponent();

    await user.type(elements.getInput(), 'john doe');

    expect(await screen.findByText(/john doe/i)).toBeInTheDocument();
  });

  it('should call "handleSetSelectedUser" when a result is clicked', async () => {
    const { elements, user, handleSetSelectedUser } = renderComponent();

    await user.type(elements.getInput(), 'john doe');

    const userSearchResult = await screen.findByText(/john doe/i);

    await user.click(userSearchResult);

    await waitFor(() => {
      expect(handleSetSelectedUser).toHaveBeenCalledWith(expect.objectContaining({ fullName: 'john doe' }));
    });
  });

  it('resets results on backspace when one char remains', async () => {
    const { elements, user } = renderComponent();

    await user.type(elements.getInput(), 'j');
    await waitFor(() => screen.getByText('john doe'));

    fireEvent.keyDown(elements.getInput(), { key: 'Backspace' });
    await waitFor(() => {
      expect(screen.queryByText('john doe')).not.toBeInTheDocument();
    });
  });

  it('should close dropdown on click away', async () => {
    const { elements, user } = renderComponent();

    await user.type(elements.getInput(), 'john');

    await screen.findByText('john doe');

    fireEvent.mouseDown(document.body);

    await waitFor(() => {
      expect(screen.queryByText('john doe')).not.toBeInTheDocument();
    });
  });
});
