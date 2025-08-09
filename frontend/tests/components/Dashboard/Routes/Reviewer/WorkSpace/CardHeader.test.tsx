import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';
import { useDispatch } from 'react-redux';
import { Mock } from 'vitest';
import userEvent from '@testing-library/user-event';
import dayjs from 'dayjs';

import { ITodoCard } from '../../../../../../src/interfaces';
import { db } from '../../../../../mocks/db';
import { getLoggedInUser } from '../../../../../utils';
import CardHeader from '../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/CardHeader';

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

describe('CardHeader', () => {
  const mockDispatch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const getProps = () => {
    const card: ITodoCard = { ...toPlainObject(db.todoCard.create()), userId: 1, todoListId: 1 };
    return { card, handleOnModalClose: vi.fn() };
  };

  const renderComponent = () => {
    const props = getProps();
    const { wrapper } = getLoggedInUser();

    render(<CardHeader {...props} />, { wrapper });

    return { props, user: userEvent.setup() };
  };

  it('should render the card title and list info', () => {
    const { props } = renderComponent();

    expect(screen.getByRole('heading', { name: `${props.card.title}` })).toBeInTheDocument();
    expect(screen.getByText(props.card.todoListTitle)).toBeInTheDocument();
  });

  it('should show date range if start and end date exist', async () => {
    const { props } = renderComponent();

    const { startDate, endDate } = props.card;

    expect(
      screen.getByText(`${dayjs(startDate).format('MMM D')}-${dayjs(endDate).format('MMM D')}`)
    ).toBeInTheDocument();
  });

  it('should render photo if present', () => {
    const { props } = renderComponent();

    const photo = screen.getByRole('img');

    expect(photo).toHaveAttribute('src', props.card.photo);
  });

  it('should switch to input mode when title is clicked', async () => {
    const { user, props } = renderComponent();

    await user.click(screen.getByRole('heading', { level: 3, name: props.card.title }));

    expect(await screen.findByRole('textbox')).toBeInTheDocument();
  });

  it('should call "updateTodoCard when input loses focus with new title"', async () => {
    const { user, props } = renderComponent();

    await user.click(screen.getByRole('heading', { level: 3, name: props.card.title }));
    const input = await screen.findByRole('textbox');

    await user.clear(input);
    await user.type(input, 'update title');
    await user.tab();

    await waitFor(() => {
      const input = screen.queryByRole('textbox');
      expect(input).not.toBeInTheDocument();
      expect(mockDispatch).toHaveBeenCalled();
    });
  });

  it('should close modal when close icon is clicked', async () => {
    const { user, props } = renderComponent();
    await user.click(screen.getByTestId('card-header-close-icon'));

    await waitFor(() => {
      expect(props.handleOnModalClose).toHaveBeenCalled();
    });
  });

  it('should render active labels from query', async () => {
    renderComponent();

    const activeLabels = await screen.findAllByTestId('ActiveLabel');
    expect(activeLabels).toHaveLength(3);
  });
});
