import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';
import userEvent from '@testing-library/user-event';

import { db } from '../../../../../mocks/db';
import { ITodoCard } from '../../../../../../src/interfaces';
import { getLoggedInUser } from '../../../../../utils';
import CardActivity from '../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/CardActivity';

describe('CardActivity', () => {
  const getProps = (overrides: Partial<ITodoCard> = {}) => {
    const card: ITodoCard = { ...toPlainObject(db.todoCard.create()), userId: 1, todoListId: 1, ...overrides };

    return { card };
  };

  const renderComponent = () => {
    const { curUser, wrapper } = getLoggedInUser();
    const props = getProps({ userId: curUser.id });

    render(<CardActivity {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      props,
    };
  };

  it('should render activity items', async () => {
    renderComponent();

    const activities = await screen.findAllByTestId('card-activity-item');

    expect(activities.length).toBe(2);
  });

  it('should delete an activity when trash icon is clicked', async () => {
    const { user } = renderComponent();

    const trashIcons = await screen.findAllByTestId('activity-trash-icon');

    await user.click(trashIcons[0]);

    const activityTitle = 'Unique hardcoded activity title';

    await waitFor(() => {
      expect(screen.queryByText(activityTitle)).not.toBeInTheDocument();
    });
  });
});
