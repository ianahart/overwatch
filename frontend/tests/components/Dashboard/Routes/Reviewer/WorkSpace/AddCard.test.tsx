import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';
import userEvent, { UserEvent } from '@testing-library/user-event';
import { useDispatch } from 'react-redux';
import { Mock } from 'vitest';

import AddCard from '../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/AddCard';
import { db } from '../../../../../mocks/db';
import { ICreateTodoCardResponse, ITodoList } from '../../../../../../src/interfaces';
import { getLoggedInUser } from '../../../../../utils';
import { server } from '../../../../../mocks/server';
import { HttpResponse, http } from 'msw';
import { baseURL } from '../../../../../../src/util';
import { createTodoCards } from '../../../../../mocks/dbActions';
import { addCardToTodoList } from '../../../../../../src/state/store';

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

describe('AddCard', () => {
  const mockDispatch = vi.fn();
  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const getFormElements = () => {
    return {
      getForm: () => screen.getByTestId('add-card-form'),
      getInput: () => screen.getByRole('textbox'),
      getOpenBtn: () => screen.getByTestId('add-card-modal-btn'),
      getAddBtn: () => screen.getByRole('button', { name: /add card/i }),
      getCloseBtn: () => screen.getByTestId('add-card-close-btn'),
    };
  };

  const submitForm = async (formElements: IFormElements, inputValue: string, user: UserEvent) => {
    const { getOpenBtn, getInput, getAddBtn } = formElements;

    await user.click(getOpenBtn());
    await user.type(getInput(), inputValue);
    await user.click(getAddBtn());
  };

  const getProps = (overrides: Partial<ITodoList> = {}) => {
    const todoListEntity = db.todoList.create();
    const todoList: ITodoList = { ...toPlainObject(todoListEntity), workSpaceId: 1, ...overrides };

    return { todoList };
  };

  const renderComponent = () => {
    const { curUser, wrapper } = getLoggedInUser();
    const props = getProps({ userId: curUser.id });

    render(<AddCard {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      props,
      formElements: getFormElements(),
    };
  };

  it('should show form when "Add card" is clicked', async () => {
    const { user, formElements } = renderComponent();

    const { getForm, getOpenBtn } = formElements;

    await user.click(getOpenBtn());

    expect(getForm()).toBeInTheDocument();
  });

  it('should submit and call the API then reset form', async () => {
    const [data] = createTodoCards(1);
    server.use(
      http.post(`${baseURL}/todo-lists/:todoListId/todo-cards`, () => {
        return HttpResponse.json<ICreateTodoCardResponse>(
          {
            message: 'success',
            data,
          },
          { status: 201 }
        );
      })
    );

    const { user, formElements } = renderComponent();

    submitForm(formElements, data.title, user);

    await waitFor(() => {
      expect(mockDispatch).toHaveBeenCalledWith(
        addCardToTodoList(
          expect.objectContaining({
            id: data.id,
            title: data.title,
          })
        )
      );
    });
  });

  it('should show server errors from the API if necessary', async () => {
    server.use(
      http.post(`${baseURL}/todo-lists/:todoListId/todo-cards`, () => {
        return HttpResponse.json(
          {
            message: 'Please provide a title',
          },
          { status: 400 }
        );
      })
    );

    const { user, formElements } = renderComponent();

    await submitForm(formElements, 'some title', user);

    expect(await screen.findByText(/please provide a title/i)).toBeInTheDocument();
  });

  it('should hide the form when the close icon button is clicked', async () => {
    const { user, formElements } = renderComponent();

    await user.click(formElements.getOpenBtn());

    expect(await screen.findByTestId('add-card-form')).toBeInTheDocument();

    await user.click(formElements.getCloseBtn());

    expect(screen.queryByTestId('add-card-form')).not.toBeInTheDocument();
  });

  it('should not submit with empty input', async () => {
    const { user, formElements } = renderComponent();

    user.click(formElements.getAddBtn());

    await waitFor(() => {
      expect(mockDispatch).not.toHaveBeenCalled();
    });
  });
});
