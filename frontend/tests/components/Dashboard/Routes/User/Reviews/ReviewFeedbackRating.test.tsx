import { screen, render, fireEvent, waitFor } from '@testing-library/react';

import ReviewFeedbackRating from '../../../../../../src/components/Dashboard/Routes/User/Reviews/ReviewFeedbackRating';
import { IReviewFeedbackFormField } from '../../../../../../src/interfaces';
import { AllProviders } from '../../../../../AllProviders';
import userEvent from '@testing-library/user-event';

vi.mock('lodash', async () => {
  const actual = await vi.importActual<typeof import('lodash')>('lodash');
  return {
    ...actual,
    debounce: <T extends (...args: any[]) => any>(fn: T, _wait?: number, _options?: any) => {
      return (...args: Parameters<T>) => fn(...args);
    },
  };
});

describe('ReviewFeedbackRating', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = (overrides = {}) => {
    const addRating = vi.fn();
    const field: IReviewFeedbackFormField = {
      title: 'Communication',
      name: 'communication',
      value: 3,
      desc: 'How well did the reviewer communicate?',
    };

    return {
      field,
      isAlreadyReviewed: false,
      addRating,
      ...overrides,
    };
  };

  const renderComponent = (overrides = {}) => {
    const props = getProps(overrides);

    render(<ReviewFeedbackRating {...props} />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      props,
    };
  };

  it('should render the title, decsription, and correct number of stars', () => {
    const { props } = renderComponent();

    const { title, desc } = props.field;
    expect(screen.getByText(title + ':')).toBeInTheDocument();
    expect(screen.getByText(desc)).toBeInTheDocument();

    expect(screen.getAllByTestId('filled-star')).toHaveLength(3);
    expect(screen.getAllByTestId('outlined-star')).toHaveLength(2);
  });

  it('should call "addRating" with correct index on mouse enter when not reviewed', async () => {
    const { props } = renderComponent();
    const stars = screen.getAllByTestId('feedback-star');
    fireEvent.mouseEnter(stars[4]);

    await waitFor(() => {
      expect(props.addRating).toHaveBeenCalledWith('communication', 5);
    });
  });

  it('does NOT call addRating if already reviewed', async () => {
    const { props } = renderComponent({ isAlreadyReviewed: true });
    const stars = screen.getAllByTestId('feedback-star');
    fireEvent.mouseEnter(stars[4]);

    await waitFor(() => {
      expect(props.addRating).not.toHaveBeenCalled();
    });
  });
});
