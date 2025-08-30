import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';

import { db } from '../../../../../../mocks/db';
import { ITodoCard } from '../../../../../../../src/interfaces';
import CardLabelsBtn from '../../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/Actions/CardLabelsBtn';
import { AllProviders } from '../../../../../../AllProviders';
import userEvent from '@testing-library/user-event';

describe('CardLabelsBtn', () => {
  const getProps = () => {
    const card: ITodoCard = { ...toPlainObject(db.todoCard.create()), userId: 1, todoListId: 1 };

    return { card };
  };

  const getElements = () => {
    return {
      getOpenBtn: () => screen.getByRole('button', { name: /labels/i }),
      getCloseBtn: () => screen.getByTestId('card-labels-close-btn'),
      getHeading: () => screen.getByRole('heading', { name: 'Labels', level: 3 }),
      getCardLabelForm: () => screen.getByTestId('CardLabelForm'),
      getOpenFormBtn: () => screen.getByRole('button', { name: /add a new label/i }),
    };
  };

  const renderComponent = () => {
    const props = getProps();

    render(<CardLabelsBtn {...props} />, { wrapper: AllProviders });

    return {
      props,
      user: userEvent.setup(),
      elements: getElements(),
    };
  };

  it('should open the dialog box when button is clicked', async () => {
    const { user, elements } = renderComponent();

    const { getOpenBtn, getHeading } = elements;

    await user.click(getOpenBtn());

    await waitFor(() => {
      expect(getHeading()).toBeInTheDocument();
    });
  });

  it('should close the dialog box when button is clicked', async () => {
    const { user, elements } = renderComponent();

    const { getOpenBtn, getCloseBtn, getHeading } = elements;

    await user.click(getOpenBtn());

    await waitFor(() => {
      expect(getHeading()).toBeInTheDocument();
    });

    await user.click(getCloseBtn());

    await waitFor(() => {
      expect(screen.queryByRole('heading', { name: 'Labels', level: 3 })).not.toBeInTheDocument();
    });
  });

  it('should switch from CardLabels to CardLabelForm when button is clicked', async () => {
    const { elements, user } = renderComponent();

    const { getOpenBtn, getCardLabelForm, getOpenFormBtn } = elements;

    await user.click(getOpenBtn());
    await user.click(getOpenFormBtn());

    await waitFor(() => {
      expect(getCardLabelForm()).toBeInTheDocument();
    });
  });

  it('should close CardLabelForm when "Close Form" is clicked', async () => {
    const { elements, user } = renderComponent();

    const { getOpenBtn, getCardLabelForm, getOpenFormBtn } = elements;

    await user.click(getOpenBtn());
    await user.click(getOpenFormBtn());

    await waitFor(() => {
      expect(getCardLabelForm()).toBeInTheDocument();
    });
    await user.click(await screen.findByTestId('card-label-form-close-icon'));

    await waitFor(() => {
      expect(screen.queryByTestId('CardLabelForm')).not.toBeInTheDocument();
    });
  });
});
