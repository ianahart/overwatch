import { screen, render } from '@testing-library/react';
import { toPlainObject } from 'lodash';

import { db } from '../../../../../mocks/db';
import { ITodoCard } from '../../../../../../src/interfaces';
import { getLoggedInUser } from '../../../../../utils';
import CardCustomFieldList from '../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/CardCustomFieldList';

describe('CardCustomFieldList', () => {
  const getProps = () => {
    const card: ITodoCard = { ...toPlainObject(db.todoCard.create()), userId: 1 };

    return { card };
  };

  const renderComponent = () => {
    const props = getProps();
    const { wrapper } = getLoggedInUser();

    render(<CardCustomFieldList {...props} />, { wrapper });
  };

  it('should render a list of CardCustomFieldItem components', async () => {
    renderComponent();
    const cardCustomFieldItems = await screen.findAllByTestId('CardCustomFieldItem');

    expect(cardCustomFieldItems.length).toBe(3);
  });
});
