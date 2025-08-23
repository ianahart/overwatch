import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';
import { useDispatch } from 'react-redux';
import { Mock } from 'vitest';
import { HttpResponse, http } from 'msw';
import userEvent from '@testing-library/user-event';

import { ITodoCard } from '../../../../../../../src/interfaces';
import { db } from '../../../../../../mocks/db';
import { getLoggedInUser } from '../../../../../../utils';
import CardRemoveBtn from '../../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/Actions/CardRemoveBtn';
import { removeTodoListTodoCard } from '../../../../../../../src/state/store';
import { server } from '../../../../../../mocks/server';
import { baseURL } from '../../../../../../../src/util';

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

describe('CardRemoveBtn', () => {
  const mockDispatch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const getProps = () => {
    const card: ITodoCard = { ...toPlainObject(db.todoCard.create()), userId: 1, todoListId: 1 };

    return { card };
  };

  const getElements = () => {
    return {
      getOpenBtn: () => screen.getByRole('button', { name: /remove/i }),
      getRemoveBtn: () => screen.getByRole('button', { name: /yes/i }),
      getCancelBtn: () => screen.getByRole('button', { name: /cancel/i }),
    };
  };

  const renderComponent = () => {
    const props = getProps();
    const { wrapper } = getLoggedInUser();

    render(<CardRemoveBtn {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      props,
      elements: getElements(),
    };
  };

  it('should render remove (open) button', () => {
    const { elements } = renderComponent();

    expect(elements.getOpenBtn()).toBeInTheDocument();
  });

  it('should open confirmation on click and closes on cancel', async () => {
    const { user, elements } = renderComponent();

    const { getOpenBtn, getCancelBtn } = elements;

    await user.click(getOpenBtn());

    expect(await screen.findByText(/are you sure you want to remove this card?/i)).toBeInTheDocument();

    await user.click(getCancelBtn());

    await waitFor(() => {
      expect(screen.queryByRole('button', { name: /cancel/i })).not.toBeInTheDocument();
    });
  });

  it('should call delete mutation and dispatch on success', async () => {
    const { user, elements, props } = renderComponent();

    const { getOpenBtn, getRemoveBtn } = elements;
    const { id, todoListId } = props.card;

    await user.click(getOpenBtn());

    await user.click(getRemoveBtn());

    await waitFor(() => {
      expect(mockDispatch).toHaveBeenCalledWith(removeTodoListTodoCard({ id, todoListId }));
    });
  });

  it('should show server error on failed delete', async () => {
    server.use(
      http.delete(`${baseURL}/todo-cards/:todoCardId`, () => {
        return HttpResponse.json(
          {
            message: 'server error during delete',
          },
          { status: 400 }
        );
      })
    );

    const { user, elements } = renderComponent();

    const { getOpenBtn, getRemoveBtn } = elements;

    await user.click(getOpenBtn());

    await user.click(getRemoveBtn());

    expect(await screen.findByText('server error during delete')).toBeInTheDocument();
  });
});
