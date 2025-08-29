import { screen, render } from '@testing-library/react';
import { toPlainObject } from 'lodash';
import { db } from '../../../../../../mocks/db';
import { ITodoCard } from '../../../../../../../src/interfaces';
import { AllProviders } from '../../../../../../AllProviders';
import CardOptions from '../../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/Actions/CardOptions';

describe('CardOptions', () => {
  const getProps = () => {
    const card: ITodoCard = { ...toPlainObject(db.todoCard.create()), userId: 1, todoListId: 1 };

    return { card };
  };

  const renderComponent = () => {
    const props = getProps();

    render(<CardOptions {...props} />, { wrapper: AllProviders });

    return {
      getHeading: () => screen.getByRole('heading', { level: 3, name: /add to card/i }),
    };
  };

  it('should render without errors and render the heading', () => {
    const { getHeading } = renderComponent();

    expect(getHeading()).toBeInTheDocument();
  });
});
