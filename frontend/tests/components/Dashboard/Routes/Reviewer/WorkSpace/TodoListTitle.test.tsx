import { screen, render, waitFor, fireEvent } from '@testing-library/react';
import { useDispatch } from 'react-redux';
import { Mock } from 'vitest';
import { ITodoCard } from '../../../../../../src/interfaces';
import { createTodoCards, createTodoLists } from '../../../../../mocks/dbActions';
import { getLoggedInUser } from '../../../../../utils';
import TodoListTitle from '../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/TodoListTitle';
import userEvent from '@testing-library/user-event';
import { updateSingleTodoList } from '../../../../../../src/state/store';
import { server } from '../../../../../mocks/server';
import { HttpResponse, http } from 'msw';
import { baseURL } from '../../../../../../src/util';

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

describe('TodoListTitle', () => {
  const createTodoList = () => {
    const cards: ITodoCard[] = createTodoCards(3);
    return createTodoLists(1, cards);
  };

  const getProps = () => {
    const [list] = createTodoList();

    return { list };
  };

  const renderComponent = () => {
    const props = getProps();

    const { wrapper } = getLoggedInUser();

    render(<TodoListTitle {...props} />, { wrapper });

    return {
      props,
      user: userEvent.setup(),
      getTitle: () => screen.getByText(props.list.title),
      getInput: () => screen.getByRole('textbox'),
    };
  };

  const mockDispatch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  it('should render the title initially', () => {
    const { getTitle } = renderComponent();

    expect(getTitle()).toBeInTheDocument();
  });

  it('should show an input on a title click', async () => {
    const { props, user, getInput, getTitle } = renderComponent();

    await user.click(getTitle());

    await waitFor(() => {
      expect(getInput()).toBeInTheDocument();
      expect(screen.getByDisplayValue(props.list.title)).toBeInTheDocument();
    });
  });

  it('should update title on blur and dispatch action', async () => {
    const { user, getInput, getTitle } = renderComponent();

    await user.click(getTitle());
    await user.clear(getInput());
    await user.type(getInput(), 'updated title');

    fireEvent.blur(getInput());

    await waitFor(() => {
      expect(mockDispatch).toHaveBeenCalledWith(
        updateSingleTodoList(expect.objectContaining({ title: 'updated title' }))
      );
    });
  });

  it('should apply server errors when necessary', async () => {
    server.use(
      http.patch(`${baseURL}/todo-lists/:id`, () => {
        return HttpResponse.json(
          {
            message: 'title is required',
          },
          { status: 400 }
        );
      })
    );
    const { user, getTitle, getInput } = renderComponent();

    await user.click(getTitle());
    await user.clear(getInput());
    await user.type(getInput(), 'test');

    fireEvent.blur(getInput());

    expect(await screen.findByText('title is required')).toBeInTheDocument();
  });

  it('should not update title if input is unchanged or empty', async () => {
    const { user, getInput, getTitle } = renderComponent();

    await user.click(getTitle());

    fireEvent.blur(getInput());

    await waitFor(() => {
      expect(mockDispatch).not.toHaveBeenCalled();
    });
  });
});
