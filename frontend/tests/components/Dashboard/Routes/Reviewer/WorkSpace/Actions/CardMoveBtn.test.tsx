import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';
import { useDispatch } from 'react-redux';
import { Mock } from 'vitest';
import { ITodoCard, ITodoList } from '../../../../../../../src/interfaces';
import { db } from '../../../../../../mocks/db';
import CardMoveBtn from '../../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/Actions/CardMoveBtn';
import { getLoggedInUser } from '../../../../../../utils';
import userEvent from '@testing-library/user-event';

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

describe('CardMoveBtn', () => {
  const mockDispatch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const getTodoCard = () => {
    const card: ITodoCard = { ...toPlainObject(db.todoCard.create()), userId: 1, todoListId: 1 };
    return card;
  };

  const getTodoLists = () => {
    const card = getTodoCard();
    const todoList: ITodoList = { ...toPlainObject(db.todoList.create()), cards: [card], workSpaceId: 1, userId: 1 };
    return [todoList];
  };

  const getProps = () => {
    const card = getTodoCard();
    return { card };
  };

  const getElements = () => ({
    getHeading: () => screen.getByRole('heading', { name: /move card/i }),
    getOpenMoveBtn: () => screen.getByRole('button', { name: /move card/i }),
    getCloseBtn: () => screen.getByTestId('move-card-close-btn'),
    getSelect: () => screen.getByTestId('move-card-select'),
    getSaveBtn: () => screen.getByRole('button', { name: /save/i }),
  });

  const renderComponent = () => {
    const props = getProps();
    const todoLists = getTodoLists();
    const { wrapper } = getLoggedInUser(
      {},
      {
        todoList: { todoLists },
      }
    );

    render(<CardMoveBtn {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      props,
      elements: getElements(),
      todoLists,
    };
  };

  it('should render the move card button', () => {
    const { elements } = renderComponent();
    expect(elements.getOpenMoveBtn()).toBeInTheDocument();
  });

  it('should open the move dialog when button is clicked', async () => {
    const { elements, user } = renderComponent();
    await user.click(elements.getOpenMoveBtn());
    expect(elements.getHeading()).toBeInTheDocument();
  });

  it('should close the dialog when the close button is clicked', async () => {
    const { elements, user } = renderComponent();
    await user.click(elements.getOpenMoveBtn());
    expect(elements.getHeading()).toBeInTheDocument();
    await user.click(elements.getCloseBtn());
    await waitFor(() => {
      expect(screen.queryByRole('heading', { name: /move card/i })).not.toBeInTheDocument();
    });
  });

  it('should update the destination list when changed in select', async () => {
    const { elements, user, todoLists } = renderComponent();
    await user.click(elements.getOpenMoveBtn());
    const [todoList] = todoLists;
    await user.selectOptions(elements.getSelect(), todoList.id.toString());
    expect((elements.getSelect() as HTMLSelectElement).value).toBe(todoList.id.toString());
  });
});
