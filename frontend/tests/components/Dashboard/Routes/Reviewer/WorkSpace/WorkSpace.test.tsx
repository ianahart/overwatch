import { screen, render, waitFor } from '@testing-library/react';
import { useDispatch } from 'react-redux';
import { Mock } from 'vitest';
import { createTodoLists, createWorkSpaces } from '../../../../../mocks/dbActions';
import { getLoggedInUser } from '../../../../../utils';
import userEvent from '@testing-library/user-event';
import WorkSpace from '../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/Workspace';

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

describe('WorkSpace', () => {
  const mockDispatch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const renderComponent = (overrides = {}) => {
    let [workSpace] = createWorkSpaces(1);
    workSpace = { ...workSpace, ...overrides };
    const todoLists = createTodoLists(2);

    const { wrapper } = getLoggedInUser(
      {},
      {
        workSpace: { workSpace },
        todoList: { todoLists },
      }
    );

    render(<WorkSpace />, { wrapper });

    return {
      user: userEvent.setup(),
      workSpace,
      todoLists,
    };
  };

  it('should render message when WorkSpace id is 0', () => {
    renderComponent({ id: 0 });

    expect(screen.getByText(/select existing workspace/i)).toBeInTheDocument();
  });

  it('should apply background color style from workspace', () => {
    renderComponent({ backgroundColor: '#123456' });

    const container = screen.getByTestId('WorkSpace');
    expect(container).toHaveStyle({ backgroundColor: 'rgb(18, 52, 86)' });
  });

  it('should dispatch setLabels on render', async () => {
    renderComponent();

    await waitFor(() => {
      expect(mockDispatch).toHaveBeenCalled();
    });
  });
});
