import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';
import { useDispatch } from 'react-redux';
import { Mock } from 'vitest';
import userEvent from '@testing-library/user-event';
import { HttpResponse, http } from 'msw';

import { getLoggedInUser } from '../../../../../utils';
import { db } from '../../../../../mocks/db';
import { IWorkSpaceEntity } from '../../../../../../src/interfaces';
import { createTodoLists } from '../../../../../mocks/dbActions';
import TodoLists from '../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/TodoLists';
import { server } from '../../../../../mocks/server';
import { baseURL } from '../../../../../../src/util';
import { mockNavigate } from '../../../../../setup';

vi.mock('../../../../../../src/util', async () => {
  const actual = await vi.importActual<typeof import('../../../../../../src/util')>('../../../../../../src/util');
  return {
    ...actual,
    retrieveTokens: () => ({ token: 'mock-token' }),
  };
});

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

describe('TodoLists', () => {
  const mockDispatch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const createWorkSpace = () => {
    const workSpace: IWorkSpaceEntity = { ...toPlainObject(db.workSpace.create()), userId: 1 };
    return workSpace;
  };

  const renderComponent = () => {
    const workSpace = createWorkSpace();
    const todoLists = createTodoLists(1);

    const { wrapper } = getLoggedInUser(
      {},
      {
        workSpace: { workSpace },
        todoList: { todoLists },
      }
    );

    render(<TodoLists />, { wrapper });

    return {
      user: userEvent.setup(),
    };
  };

  it('should dispatch "setTodoLists" and render lists after fetch', async () => {
    renderComponent();

    const todoLists = await screen.findAllByTestId('TodoList');

    expect(todoLists).toHaveLength(1);

    await waitFor(() => {
      expect(mockDispatch).toHaveBeenCalled();
    });
  });

  it('should navigate to error page if request fails', async () => {
    server.use(
      http.get(`${baseURL}/workspaces/:workSpaceId/todo-lists`, () => {
        return HttpResponse.json(
          {
            message: 'error',
          },
          { status: 400 }
        );
      })
    );
    renderComponent();

    await waitFor(() => {
      expect(mockNavigate).toHaveBeenCalledWith('*', expect.anything());
    });
  });
});
