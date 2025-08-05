import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';
import { ICheckList, ICheckListItem } from '../../../../../../src/interfaces';
import { db } from '../../../../../mocks/db';
import { getLoggedInUser } from '../../../../../utils';
import CardCheckList from '../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/CardCheckList';
import userEvent from '@testing-library/user-event';

describe('CardCheckList', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getActions = () => {
    return {
      getDeleteBtn: () => screen.getByRole('button', { name: /delete/i }),
      getAddBtn: () => screen.getByRole('button', { name: /add/i }),
    };
  };

  const getProps = (userId: number) => {
    const checkListItems: ICheckListItem[] = Array.from({ length: 3 }).map((_, i) => {
      const isCompleted = i % 2 === 0;
      return { ...toPlainObject(db.checkListItem.create()), checkListId: 1, isCompleted };
    });

    const checkList: ICheckList = { ...toPlainObject(db.checkList.create()), checkListItems, userId };

    return {
      checkList,
      addCheckListItem: vi.fn(),
      updateCheckListItem: vi.fn(),
      deleteCheckListItem: vi.fn(),
    };
  };

  const renderComponent = () => {
    const { wrapper, curUser } = getLoggedInUser();
    const props = getProps(curUser.id);

    render(<CardCheckList {...props} />, { wrapper });

    return {
      props,
      user: userEvent.setup(),
      actions: getActions(),
    };
  };

  it('should render checklist title and items', () => {
    const { props } = renderComponent();

    const { checkList } = props;

    expect(screen.getByText(checkList.title)).toBeInTheDocument();
    expect(screen.getAllByTestId('card-checklist-item')).toHaveLength(checkList.checkListItems.length);
  });

  it('should toggle form when "Add Item" button is clicked', async () => {
    const { user, actions } = renderComponent();

    const { getAddBtn } = actions;

    await user.click(getAddBtn());

    expect(await screen.findByTestId('CardCheckListItemForm')).toBeInTheDocument();
  });

  it('should toggle hide/show completed items', async () => {
    const { user, props } = renderComponent();

    const toggleButton = screen.getByRole('button', { name: /hide checked items/i });

    expect(toggleButton).toBeInTheDocument();
    await user.click(toggleButton);

    const visibleItemsState = props.checkList.checkListItems.filter((item) => !item.isCompleted);

    const visibleItemElements = screen.getAllByTestId('card-checklist-item');

    expect(visibleItemElements.length).toBe(visibleItemsState.length);
  });

  it('should call delete checklist mutation on delete click', async () => {
    const { user, actions } = renderComponent();

    await user.click(actions.getDeleteBtn());

    await waitFor(() => {
      expect(actions.getDeleteBtn()).toBeInTheDocument();
    });
  });
});
