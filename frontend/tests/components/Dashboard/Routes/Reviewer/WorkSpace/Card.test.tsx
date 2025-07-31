import { screen, render } from '@testing-library/react';
import { toPlainObject } from 'lodash';

import { ITodoCard } from '../../../../../../src/interfaces';
import { db } from '../../../../../mocks/db';
import { getLoggedInUser } from '../../../../../utils';
import Card from '../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/Card';
import userEvent from '@testing-library/user-event';
import dayjs from 'dayjs';

describe('Card', () => {
  const getProps = (overrides: Partial<ITodoCard> = {}) => {
    const data: ITodoCard = { ...toPlainObject(db.todoCard.create()), userId: 1, todoListId: 1, ...overrides };

    return { data };
  };

  const renderComponent = () => {
    const { curUser, wrapper } = getLoggedInUser();
    const props = getProps({ userId: curUser.id });

    render(<Card {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      props,
    };
  };

  it('should render card title and date range', async () => {
    const { props } = renderComponent();

    let { title, startDate, endDate } = props.data;

    startDate = dayjs(startDate).format('MMM D');
    endDate = dayjs(endDate).format('MMM D');

    expect(await screen.findByText(title)).toBeInTheDocument();
    expect(await screen.findByText(`${startDate}-${endDate}`)).toBeInTheDocument();
  });

  it('should render active labels when fetched', async () => {
    renderComponent();

    const activeLabels = await screen.findAllByTestId('Card');

    expect(activeLabels.length).toBe(1);
  });

  it('should show edit icon on hover', async () => {
    const { user, props } = renderComponent();

    await user.hover(await screen.findByText(props.data.title));

    expect(await screen.findByTestId('card-edit-icon')).toBeInTheDocument();
  });

  it('should open and close modal on click', async () => {
    const { user } = renderComponent();

    await user.click(await screen.findByTestId('Card'));

    expect(await screen.findByTestId('CardModal')).toBeInTheDocument();
  });
});
