import { screen, render, within, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';

import { db } from '../../../../../mocks/db';
import { ITodoCard } from '../../../../../../src/interfaces';
import { getLoggedInUser } from '../../../../../utils';
import CardCheckLists from '../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/CardCheckLists';
import userEvent from '@testing-library/user-event';

describe('CardCheckLists', () => {
  beforeEach(() => {
    db.checkListItem.delete({ where: { id: { lte: 999 } } });
  });

  const getProps = () => {
    const card: ITodoCard = { ...toPlainObject(db.todoCard.create()), userId: 1 };

    return { card };
  };

  const renderComponent = () => {
    const { wrapper } = getLoggedInUser();
    const props = getProps();

    render(<CardCheckLists {...props} />, { wrapper });

    return {
      props,
      user: userEvent.setup(),
    };
  };

  it('should show a loading spinner initially', () => {
    renderComponent();
    expect(screen.getByText(/loading checklists/i)).toBeInTheDocument();
  });

  it('should render checklist title after data loads', async () => {
    renderComponent();

    expect(await screen.findByRole('heading', { level: 3 })).toBeInTheDocument();
  });

  it('should update a checklist item title', async () => {
    const { user } = renderComponent();

    await screen.findByRole('heading', { level: 3 });

    const checkListItems = await screen.findAllByTestId('card-checklist-item');

    const checkListItemEditButton = within(checkListItems[0]).getByTestId('card-checklist-item-edit-icon');

    await user.click(checkListItemEditButton);

    const input = within(checkListItems[0]).getByTestId('card-checklist-item-edit-input');
    expect(input).toBeInTheDocument();

    await user.clear(input);
    await user.type(input, 'updated title');
    input.blur();
  });

  it('should delete a checklist item when delete is clicked', async () => {
    const { user } = renderComponent();

    await screen.findByRole('heading', { level: 3 });

    const checkListItems = await screen.findAllByTestId('card-checklist-item');

    const checkListItemDeleteButton = within(checkListItems[0]).getByTestId('card-checklist-item-delete-icon');
    expect(checkListItemDeleteButton).toBeInTheDocument();
    await user.click(checkListItemDeleteButton);

    await waitFor(() => {
      const updatedCheckListItems = screen.queryAllByTestId('card-checklist-item');

      expect(updatedCheckListItems.length).toBe(checkListItems.length - 1);
    });
  });
});
