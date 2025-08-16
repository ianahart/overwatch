import { screen, render, waitFor } from '@testing-library/react';
import { useDispatch } from 'react-redux';
import { toPlainObject } from 'lodash';
import { Mock } from 'vitest';
import { getLoggedInUser } from '../../../../../utils';
import { ITodoList, IWorkSpaceEntity } from '../../../../../../src/interfaces';
import { db } from '../../../../../mocks/db';
import TodoListOptions from '../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/TodoListOptions';
import userEvent from '@testing-library/user-event';
import { deleteSingleTodoList } from '../../../../../../src/state/store';

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

describe('TodoListOptions', () => {
  const mockDispatch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const getProps = () => {
    return {
      todoListId: 1,
      onClickClose: vi.fn(),
    };
  };

  const renderComponent = () => {
    const workSpace: IWorkSpaceEntity = { ...toPlainObject(db.workSpace.create()), userId: 1 };
    const todoList: ITodoList = { ...toPlainObject(db.todoList.create()), userId: 1, workSpaceId: workSpace.id };

    const { wrapper } = getLoggedInUser(
      {},
      {
        todoList: { todoLists: [todoList] },
        workSpace: { workSpace: workSpace },
      }
    );

    const props = getProps();

    render(<TodoListOptions {...props} />, { wrapper });

    return {
      props,
      user: userEvent.setup(),
      getRemoveButton: () => screen.getByRole('button', { name: /remove/i }),
    };
  };

  it('should render remove button', () => {
    const { getRemoveButton } = renderComponent();

    expect(getRemoveButton()).toBeInTheDocument();
  });

  it('should delete a todolist and dispatch actions and call onClickClose', async () => {
    const { user, getRemoveButton, props } = renderComponent();

    await user.click(getRemoveButton());

    await waitFor(() => {
      expect(mockDispatch).toHaveBeenCalledWith(deleteSingleTodoList(props.todoListId));
      expect(props.onClickClose).toHaveBeenCalled();
      expect(screen.getByText(/your todo list was deleted./i)).toBeInTheDocument();
    });
  });
});
