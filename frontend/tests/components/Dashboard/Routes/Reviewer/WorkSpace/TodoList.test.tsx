import { screen, render } from '@testing-library/react';
import { toPlainObject } from 'lodash';

import { ITodoCard, ITodoList } from '../../../../../../src/interfaces';
import { db } from '../../../../../mocks/db';
import TodoList from '../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/TodoList';
import { AllProviders } from '../../../../../AllProviders';
import userEvent from '@testing-library/user-event';
import { createTodoCards } from '../../../../../mocks/dbActions';

describe('TodoList', () => {
  const createList = (cards: ITodoCard[]) => {
    const list: ITodoList = { ...toPlainObject(db.todoList.create()), cards, workSpaceId: 1, userId: 1 };
    return { list };
  };

  const getProps = () => {
    const cards: ITodoCard[] = createTodoCards(2);
    const { list } = createList(cards);
    return { list };
  };

  const renderComponent = () => {
    const props = getProps();

    render(<TodoList {...props} />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      props,
    };
  };

  it('should render the todo list title', () => {
    const { props } = renderComponent();

    expect(screen.getByText(props.list.title)).toBeInTheDocument();
  });

  it('should open  TodoListOptions menu when clicking three dots', async () => {
    const { props, user } = renderComponent();

    const threeDots = screen.getByTestId('todolist-options-trigger');
    await user.click(threeDots);

    expect(screen.getByTestId('TodoListOptions')).toBeInTheDocument();

    await user.click(screen.getByText(props.list.title));

    expect(screen.queryByTestId('TodoListOptions')).not.toBeInTheDocument();
  });

  it('should render cards when present', () => {
    const { props } = renderComponent();

    const { cards } = props.list;

    cards.forEach((card) => {
      expect(screen.getByText(card.title)).toBeInTheDocument();
    });
  });

  it('applies DnD props (style from useSortable)', () => {
    const cards: ITodoCard[] = createTodoCards(2);
    const { list } = createList(cards);
    render(<TodoList list={list} />, { wrapper: AllProviders });

    const container = screen.getByText(list.title).closest('div');

    expect(container?.style.transform).toBeDefined();
    expect(container?.style.transition).toBeDefined();
  });
});
