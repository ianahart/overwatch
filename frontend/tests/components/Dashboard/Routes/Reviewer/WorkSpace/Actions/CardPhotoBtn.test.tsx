import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';
import { useDispatch } from 'react-redux';
import { Mock } from 'vitest';
import { ITodoCard } from '../../../../../../../src/interfaces';
import { db } from '../../../../../../mocks/db';
import CardPhotoBtn from '../../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/Actions/CardPhotoBtn';
import { getLoggedInUser } from '../../../../../../utils';
import userEvent from '@testing-library/user-event';

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

describe('CardPhotoBtn', () => {
  const mockDispatch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const getProps = () => {
    const card: ITodoCard = { ...toPlainObject(db.todoCard.create()), userId: 1, todoListId: 1 };

    return { card };
  };

  const renderComponent = () => {
    const props = getProps();
    const { wrapper } = getLoggedInUser();

    render(<CardPhotoBtn {...props} />, { wrapper });

    return {
      props,
      user: userEvent.setup(),
      getButton: () => screen.getByRole('button', { name: /cover photo/i }),
    };
  };

  it('should render cover photo button', () => {
    const { getButton } = renderComponent();

    expect(getButton()).toBeInTheDocument();
  });

  it('should close library when "onClickAway" is triggered', async () => {
    const { user, getButton } = renderComponent();

    await user.click(getButton());

    await user.click(document.body);

    await waitFor(() => {
      expect(screen.queryByText(/search for a photo/i)).not.toBeInTheDocument();
    });
  });

  it('should dispatch when "updateCardPhoto is used"', async () => {
    const { user, getButton } = renderComponent();

    await user.click(getButton());

    const photoDiv = screen.getByText(/search for a photo/i).closest('div');
    expect(photoDiv).toBeInTheDocument();
  });
});
