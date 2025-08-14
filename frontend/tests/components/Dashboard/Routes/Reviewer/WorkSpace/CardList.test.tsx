import { screen, render } from '@testing-library/react';
import { toPlainObject } from 'lodash';

import { ITodoCard } from '../../../../../../src/interfaces';
import { db } from '../../../../../mocks/db';
import CardList from '../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/CardList';
import { AllProviders } from '../../../../../AllProviders';

describe('CardList', () => {
  const getProps = () => {
    const cards: ITodoCard[] = [];

    for (let i = 0; i < 3; i++) {
      const card: ITodoCard = { ...toPlainObject(db.todoCard.create()), userId: 1, todoListId: 1 };
      cards.push(card);
    }

    return { cards };
  };

  const renderComponent = () => {
    const props = getProps();

    render(<CardList {...props} />, { wrapper: AllProviders });

    return {
      props,
    };
  };

  it('should render a list of cards', () => {
    const { props } = renderComponent();

    const cardElements = screen.getAllByTestId('Card');

    expect(cardElements).toHaveLength(props.cards.length);
  });
});
