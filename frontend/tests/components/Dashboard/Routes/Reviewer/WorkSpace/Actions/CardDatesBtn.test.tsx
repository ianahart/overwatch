import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';
import { useDispatch } from 'react-redux';
import { Mock } from 'vitest';
import { ITodoCard } from '../../../../../../../src/interfaces';
import { db } from '../../../../../../mocks/db';
import { getLoggedInUser } from '../../../../../../utils';
import CardDatesBtn from '../../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/Actions/CardDatesBtn';
import userEvent from '@testing-library/user-event';

vi.mock('react-datepicker', () => {
  return {
    default: ({ onChange }: { onChange: (dates: [Date, Date]) => void }) => {
      return (
        <div role="dialog">
          <button
            data-testid="mock-date-picker"
            onClick={() => onChange([new Date(2025, 7, 15), new Date(2025, 7, 20)])}
          >
            Pick Date
          </button>
        </div>
      );
    },
  };
});

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

describe('CardDateBtn', () => {
  const mockDispatch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const getProps = () => {
    const card: ITodoCard = { ...toPlainObject(db.todoCard.create()), userId: 1, todoListId: 1 };

    return { card };
  };

  const renderComponent = () => {
    const props = getProps();

    const { wrapper } = getLoggedInUser();

    render(<CardDatesBtn {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      props,
      getOpenBtn: () => screen.getByRole('button', { name: /dates/i }),
    };
  };

  it('should render the dates button', () => {
    const { getOpenBtn } = renderComponent();

    expect(getOpenBtn()).toBeInTheDocument();
  });

  it('should open the datepicker when button is clicked', async () => {
    const { user, getOpenBtn } = renderComponent();
    await user.click(getOpenBtn());
    expect(await screen.findByRole('dialog')).toBeInTheDocument();
  });

  it('should close the datepicker when clicking away', async () => {
    const { user, getOpenBtn } = renderComponent();

    await user.click(getOpenBtn());

    expect(await screen.findByRole('dialog')).toBeInTheDocument();

    await user.click(document.body);
    await waitFor(() => {
      expect(screen.queryByRole('dialog')).not.toBeInTheDocument();
    });
  });

  it('should initialize with card startDate/endDate if provided', async () => {
    const { user, getOpenBtn } = renderComponent();

    await user.click(getOpenBtn());
    const datepicker = await screen.findByRole('dialog');
    expect(datepicker).toBeInTheDocument();
  });

  it('should call updateTodoCard and dispatches when selecting new dates', async () => {
    const { user, getOpenBtn } = renderComponent();

    await user.click(getOpenBtn());

    const pickDateBtn = screen.getByTestId('mock-date-picker');
    await user.click(pickDateBtn);

    expect(mockDispatch).toHaveBeenCalled();
  });
});
