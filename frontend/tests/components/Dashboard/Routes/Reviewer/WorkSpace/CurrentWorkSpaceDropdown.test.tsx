import { screen, render } from '@testing-library/react';
import { useDispatch } from 'react-redux';
import { Mock } from 'vitest';

import { mockNavigate } from '../../../../../setup';
import { getLoggedInUser } from '../../../../../utils';
import CurrentWorkSpaceDropdown from '../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/CurrentWorkSpaceDropdown';
import userEvent from '@testing-library/user-event';
import { setWorkSpace } from '../../../../../../src/state/store';

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

describe('CurrentWorkSpaceDropdown', () => {
  const mockDispatch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const getProps = () => {
    return {
      onClickAway: vi.fn(),
      handleSetOpen: vi.fn(),
    };
  };

  const renderComponent = () => {
    const props = getProps();
    const { curUser, wrapper } = getLoggedInUser();

    render(<CurrentWorkSpaceDropdown {...props} />, { wrapper });

    return {
      props,
      user: userEvent.setup(),
      curUser,
    };
  };

  it('should render initial workspaces from query', async () => {
    renderComponent();

    const workSpaces = await screen.findAllByTestId('workspace-item');

    expect(workSpaces).toHaveLength(2);
  });

  it('should paginate when clicking "See more..."', async () => {
    const { user } = renderComponent();

    await user.click(await screen.findByRole('button', { name: /see more/i }));

    const workSpaces = await screen.findAllByTestId('workspace-item');

    expect(workSpaces).toHaveLength(4);
  });

  it('should dispatch, close and navigate when clicking on a workspace', async () => {
    const { user, props, curUser } = renderComponent();

    const workSpaces = await screen.findAllByTestId('workspace-item');

    await user.click(workSpaces[0]);

    expect(mockDispatch).toHaveBeenCalledWith(setWorkSpace(expect.objectContaining({ userId: 1 })));
    expect(props.handleSetOpen).toHaveBeenCalledWith(false);
    expect(mockNavigate).toHaveBeenCalledWith(
      expect.stringContaining(`/dashboard/${curUser.slug}/reviewer/workspaces/`)
    );
  });
});
