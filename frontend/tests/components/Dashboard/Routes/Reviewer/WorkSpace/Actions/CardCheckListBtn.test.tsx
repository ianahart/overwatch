import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';
import { ITodoCard } from '../../../../../../../src/interfaces';
import { db } from '../../../../../../mocks/db';
import { getLoggedInUser } from '../../../../../../utils';
import CardCheckListBtn from '../../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/Actions/CardCheckListBtn';
import userEvent from '@testing-library/user-event';

describe('CardCheckListBtn', () => {
  const getProps = () => {
    const card: ITodoCard = { ...toPlainObject(db.todoCard.create()), userId: 1, todoListId: 1 };

    return { card };
  };

  const getElements = () => {
    return {
      getOpenBtn: () => screen.getByRole('button', { name: /checklist/i }),
      getTitle: () => screen.getByRole('heading', { name: /add checklist/i }),
      getInput: () => screen.getByRole('textbox'),
      getAddBtn: () => screen.getByRole('button', { name: /add/i }),
      getCancelBtn: () => screen.getByRole('button', { name: /cancel/i }),
    };
  };

  const renderComponent = () => {
    const props = getProps();
    const { wrapper } = getLoggedInUser();

    render(<CardCheckListBtn {...props} />, { wrapper });

    return {
      props,
      user: userEvent.setup(),
      elements: getElements(),
    };
  };

  it('should render the checklist button', () => {
    const { elements } = renderComponent();

    const { getOpenBtn } = elements;

    expect(getOpenBtn()).toBeInTheDocument();
  });

  it('should open the ClickAway panel on button click', async () => {
    const { user, elements } = renderComponent();

    const { getOpenBtn, getInput } = elements;

    await user.click(getOpenBtn());

    expect(getInput()).toBeInTheDocument();
  });

  it('should close the ClickAway panel on button click', async () => {
    const { user, elements } = renderComponent();

    const { getOpenBtn, getCancelBtn, getInput } = elements;

    await user.click(getOpenBtn());

    await waitFor(() => {
      expect(getInput()).toBeInTheDocument();
    });

    await user.click(getCancelBtn());

    await waitFor(() => {
      expect(screen.queryByRole('textbox')).not.toBeInTheDocument();
    });
  });

  it('should update input field value on change', async () => {
    const { user, elements } = renderComponent();

    const { getInput, getOpenBtn } = elements;

    await user.click(getOpenBtn());

    await user.type(getInput(), 'checklist 1');
    expect((getInput() as HTMLInputElement).value).toBe('checklist 1');
  });

  it('should submit the form successfully and close clickAway', async () => {
    const { user, elements } = renderComponent();

    const { getInput, getAddBtn, getOpenBtn } = elements;

    await user.click(getOpenBtn());
    await user.type(getInput(), 'checklist 1');
    await user.click(getAddBtn());

    await waitFor(() => {
      expect(screen.queryByRole('textbox')).not.toBeInTheDocument();
    });
  });
});
