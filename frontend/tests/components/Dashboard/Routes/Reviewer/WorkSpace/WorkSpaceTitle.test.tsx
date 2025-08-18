import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';
import userEvent from '@testing-library/user-event';
import { useDispatch } from 'react-redux';
import { Mock } from 'vitest';
import { getLoggedInUser } from '../../../../../utils';
import WorkSpaceTitle from '../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/WorkspaceTitle';
import { IWorkSpaceEntity } from '../../../../../../src/interfaces';
import { db } from '../../../../../mocks/db';
import { setWorkSpace, updateWorkSpaceProperty } from '../../../../../../src/state/store';

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

describe('WorkSpaceTitle', () => {
  const mockDispatch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const renderComponent = (overrides = {}) => {
    const workSpace: IWorkSpaceEntity = { ...toPlainObject(db.workSpace.create()), ...overrides };
    const { wrapper } = getLoggedInUser(
      {},
      {
        workSpace: { workSpace },
      }
    );

    render(<WorkSpaceTitle />, { wrapper });

    return {
      user: userEvent.setup(),
      workSpace,
      getPlusIcon: () => screen.getByTestId('workspace-title-plus-icon'),
      getTrashIcon: () => screen.getByTestId('workspace-title-trash-icon'),
      getInput: () => screen.getByRole('textbox'),
    };
  };

  it('should show placeholder when no title', () => {
    renderComponent({ title: '' });

    expect(screen.getByText(/add workspace title/i)).toBeInTheDocument();
  });

  it('should toggle to input when title clicked', async () => {
    const { user, workSpace, getInput } = renderComponent();

    await user.click(screen.getByText(workSpace.title));

    expect(getInput()).toBeInTheDocument();
  });

  it('should create workspace on blur when id=0', async () => {
    const { user, getInput } = renderComponent({ id: 0, title: '' });

    await user.click(screen.getByText(/add workspace title/i));

    await user.type(getInput(), 'workspace test');
    getInput().blur();

    await waitFor(() => {
      expect(mockDispatch).toHaveBeenCalledWith(
        updateWorkSpaceProperty({ value: 'workspace test', property: 'title' })
      );
    });
  });

  it('should update workspace on blur when id !=0', async () => {
    const { user, getInput, workSpace } = renderComponent({ id: 5, title: 'old title' });

    await user.click(screen.getByText(workSpace.title));

    await user.clear(getInput());

    await user.type(getInput(), 'update title');
    getInput().blur();

    await waitFor(() => {
      expect(mockDispatch).toHaveBeenCalledWith(setWorkSpace(expect.objectContaining({ title: 'updated title' })));
    });
  });

  it('should call "deleteWorkSpace" when trash icon is clicked', async () => {
    const { user, getTrashIcon } = renderComponent();

    await user.click(getTrashIcon());

    await waitFor(() => {
      expect(mockDispatch).toHaveBeenCalledWith(
        setWorkSpace({ userId: 0, id: 0, createdAt: '', title: '', backgroundColor: '' })
      );
    });
  });
});
