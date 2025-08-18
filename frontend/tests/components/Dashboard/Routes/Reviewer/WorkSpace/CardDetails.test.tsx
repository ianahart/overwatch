import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';
import { useDispatch } from 'react-redux';
import { Mock } from 'vitest';
import userEvent from '@testing-library/user-event';

import { ITodoCard } from '../../../../../../src/interfaces';
import { db } from '../../../../../mocks/db';
import { getLoggedInUser } from '../../../../../utils';
import CardDetails from '../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/CardDetails';
import { updateTodoListTodoCard } from '../../../../../../src/state/store';

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

describe('CardDetails', () => {
  const mockDispatch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const getElements = () => {
    return {
      getEditBtn: () => screen.getByRole('button', { name: /edit/i }),
      getSaveBtn: () => screen.getByRole('button', { name: /save/i }),
      getCancelBtn: () => screen.getByRole('button', { name: /cancel/i }),
    };
  };

  const getProps = () => {
    const card: ITodoCard = { ...toPlainObject(db.todoCard.create()) };

    return { card };
  };

  const renderComponent = () => {
    const props = getProps();
    const { wrapper } = getLoggedInUser();

    render(<CardDetails {...props} />, { wrapper });

    return {
      props,
      elements: getElements(),
      user: userEvent.setup(),
    };
  };

  it('should render initial state with "Edit" button and placeholder text', () => {
    const { elements } = renderComponent();

    const { getEditBtn } = elements;

    expect(screen.getByRole('heading', { name: /details/i, level: 3 })).toBeInTheDocument();
    expect(getEditBtn()).toBeInTheDocument();
  });

  it('should enter "Edit" mode when the edit button is clicked', async () => {
    const { user, elements } = renderComponent();

    const { getEditBtn, getSaveBtn, getCancelBtn } = elements;

    await user.click(getEditBtn());

    await waitFor(() => {
      expect(getSaveBtn()).toBeInTheDocument();
      expect(getCancelBtn()).toBeInTheDocument();
    });
  });

  it('should submit successfully and exists edit mode', async () => {
    const { user, elements } = renderComponent();

    const { getEditBtn, getSaveBtn } = elements;

    await user.click(getEditBtn());

    localStorage.setItem('details', 'updated title');

    await user.click(getSaveBtn());

    await waitFor(() => {
      expect(mockDispatch).toHaveBeenCalledWith(
        updateTodoListTodoCard(expect.objectContaining({ title: 'updated title' }))
      );
    });
  });

  it('should cancel "Edit" mode and clear "localStorage"', async () => {
    const { user, elements } = renderComponent();

    const { getEditBtn, getCancelBtn } = elements;
    await user.click(getEditBtn());

    localStorage.setItem('details', 'Some details');

    await user.click(getCancelBtn());

    expect(localStorage.getItem('details')).toBeNull();

    expect(screen.queryByRole('button', { name: /save/i })).not.toBeInTheDocument();
  });
});
