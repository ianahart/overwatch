import { screen, render, waitFor } from '@testing-library/react';
import userEvent, { UserEvent } from '@testing-library/user-event';
import { toPlainObject } from 'lodash';

import { db } from '../../mocks/db';
import { IReview, IUser } from '../../../src/interfaces';
import { getLoggedInUser } from '../../utils';
import ReviewDisplay from '../../../src/components/Review/ReviewDisplay';
import { mockNavigate } from '../../setup';

describe('ReviewDisplay', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const openMenu = async (user: UserEvent) => {
    const trigger = screen.getByTestId('review-display-menu-container');
    await user.click(trigger);
  };

  const getProps = (curUser: IUser, overrides = {}) => {
    const reviewEntity = toPlainObject(db.review.create());

    const review: IReview = {
      ...reviewEntity,
      authorId: curUser.id,
      isEdited: true,
      name: curUser.fullName,
    };

    return {
      review,
      currentUserId: curUser.id,
      avatarUrl: curUser.avatarUrl,
      reviewerId: curUser.id,
      fullName: curUser.fullName,
      ...overrides,
    };
  };

  const renderComponent = () => {
    const { curUser, wrapper } = getLoggedInUser();

    const props = getProps(toPlainObject(curUser));

    render(<ReviewDisplay {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      props,
    };
  };

  it('should render the review content correctly', () => {
    const { props } = renderComponent();

    const { name, rating, review } = props.review;

    expect(screen.getByText(name)).toBeInTheDocument();
    expect(screen.getByText(rating)).toBeInTheDocument();
    expect(screen.getByText(review)).toBeInTheDocument();
  });

  it('should show edit/delete menu on trigger click and deletes review', async () => {
    const { user } = renderComponent();

    await openMenu(user);

    expect(await screen.findByTestId('review-display-menu-edit')).toBeInTheDocument();

    const deleteButton = await screen.findByTestId('review-display-menu-delete');
    await user.click(deleteButton);

    await waitFor(() => {
      expect(screen.queryByTestId('review-display-menu-delete')).not.toBeInTheDocument();
    });
  });

  it('should navigate to edit page with correct state on edit', async () => {
    const { props, user } = renderComponent();

    const { reviewerId, review, avatarUrl, fullName } = props;

    await openMenu(user);

    const editButton = await screen.findByTestId('review-display-menu-edit');

    await user.click(editButton);

    await waitFor(() => {
      expect(mockNavigate).toHaveBeenCalledWith(`/reviews/${review.id}/edit`, {
        state: { fullName, avatarUrl, reviewerId, authorId: review.authorId },
      });
    });
  });

  it('should hide the menu when clicking outside', async () => {
    const { user } = renderComponent();

    await openMenu(user);

    expect(screen.getByText('Edit')).toBeInTheDocument();

    await user.click(document.body);
    await waitFor(() => {
      expect(screen.queryByText('Edit')).not.toBeInTheDocument();
    });
  });
});
