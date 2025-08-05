import { screen, render, waitFor } from '@testing-library/react';
import { useDispatch } from 'react-redux';
import { Mock } from 'vitest';
import { toPlainObject } from 'lodash';

import AddTodoList from '../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/AddTodoList';
import { ICreateTodoListResponse, ITodoList, IWorkSpaceEntity } from '../../../../../../src/interfaces';
import { db } from '../../../../../mocks/db';
import { getLoggedInUser } from '../../../../../utils';
import userEvent, { UserEvent } from '@testing-library/user-event';
import { server } from '../../../../../mocks/server';
import { HttpResponse, http } from 'msw';
import { baseURL } from '../../../../../../src/util';
import { addToTodoList } from '../../../../../../src/state/store';

export interface IFormElements {
  getForm: () => HTMLElement;
  getInput: () => HTMLElement;
  getOpenBtn: () => HTMLElement;
  getAddBtn: () => HTMLElement;
  getCloseBtn: () => HTMLElement;
}

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

describe('AddTodoList', () => {
  const mockDispatch = vi.fn();
  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const submitForm = async (user: UserEvent, formElements: IFormElements, inputValue: string) => {
    const { getInput, getOpenBtn, getAddBtn } = formElements;

    await user.click(getOpenBtn());
    await user.type(getInput(), inputValue);
    await user.click(getAddBtn());
  };

  const getFormElements = () => {
    return {
      getOpenBtn: () => screen.getByTestId('add-todolist-open-dropdown-btn'),
      getCloseBtn: () => screen.getByTestId('add-todolist-close-dropdown-btn'),
      getForm: () => screen.getByTestId('add-todolist-form'),
      getInput: () => screen.getByRole('textbox'),
      getAddBtn: () => screen.getByRole('button', { name: /add list/i }),
    };
  };

  const getState = () => {
    const workSpace: IWorkSpaceEntity = { ...toPlainObject(db.workSpace.create()), userId: 1 };
    const todoList: ITodoList = {
      ...toPlainObject(db.todoList.create()),
      userId: 1,
      title: 'test title',
      workSpaceId: workSpace.id,
      cards: [],
    };

    return {
      workSpace,
      todoLists: [todoList],
    };
  };

  const renderComponent = () => {
    const { workSpace, todoLists } = getState();
    const { wrapper } = getLoggedInUser(
      {},
      {
        workSpace: {
          workSpace,
        },
        todoList: {
          todoLists,
        },
      }
    );

    render(<AddTodoList />, { wrapper });

    return {
      user: userEvent.setup(),
      todoLists,
      workSpace,
      formElements: getFormElements(),
    };
  };

  it('should show input form when "Add another list" is clicked', async () => {
    const { user, formElements } = renderComponent();

    const { getForm, getOpenBtn } = formElements;

    await user.click(getOpenBtn());

    expect(getForm()).toBeInTheDocument();
  });

  it('should submit the form and trigger an API Call then dispatch "AddTodoList"', async () => {
    const [data] = getState().todoLists;

    server.use(
      http.post(`${baseURL}/workspaces/:workSpaceId/todo-lists`, () => {
        return HttpResponse.json<ICreateTodoListResponse>(
          {
            message: 'Please provide a title',
            data,
          },
          { status: 201 }
        );
      })
    );

    const { user, formElements } = renderComponent();

    await submitForm(user, formElements, data.title);

    expect(mockDispatch).toHaveBeenCalledWith(
      addToTodoList(
        expect.objectContaining({
          id: data.id,
          title: data.title,
        })
      )
    );
  });

  it('should submit the form and trigger an API Call then dispatch "AddTodoList"', async () => {
    server.use(
      http.post(`${baseURL}/workspaces/:workSpaceId/todo-lists`, () => {
        return HttpResponse.json(
          {
            message: 'Please provide a title',
          },
          { status: 400 }
        );
      })
    );

    const { user, formElements } = renderComponent();

    await submitForm(user, formElements, 'test title');

    expect(await screen.findByText(/please provide a title/i)).toBeInTheDocument();
  });

  it('should not submit an empty input', async () => {
    const { user, formElements } = renderComponent();

    const { getOpenBtn, getAddBtn } = formElements;

    await user.click(getOpenBtn());
    await user.click(getAddBtn());

    await waitFor(() => {
      expect(mockDispatch).not.toHaveBeenCalled();
    });
  });

  it('should close form when close icon is clicked', async () => {
    const { user, formElements } = renderComponent();

    const { getOpenBtn, getCloseBtn, getForm } = formElements;

    await user.click(getOpenBtn());

    expect(getForm()).toBeInTheDocument();

    await user.click(getCloseBtn());

    expect(screen.queryByTestId('add-todolist-form')).not.toBeInTheDocument();
  });
});
