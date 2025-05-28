import { screen, render, waitFor } from '@testing-library/react';

import ActionReview from '../../../src/components/Review/ActionReview';
import { getLoggedInUser } from '../../utils';
import { db } from '../../mocks/db';
import userEvent from '@testing-library/user-event';
import { IUser } from '../../../src/interfaces';
import { toPlainObject } from 'lodash';
import { mockNavigate } from '../../setup';

describe('ActionReview', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    [1, 2].forEach((id) => {
      try {
        db.user.delete({ where: { id: { equals: id } } });
      } catch (e) {
        console.log(e);
      }
    });
    db.review.delete({ where: { id: { equals: 1 } } });
  });
  const getProps = (curUser: IUser, overrides = {}) => {
    const review = db.review.create();
    const author = db.user.create();

    return {
      action: 'create',
      authorId: author.id,
      avatarUrl: author.avatarUrl,
      reviewerId: curUser.id,
      fullName: author.fullName,
      reviewId: review.id,
      ...overrides,
    };
  };

  const getForm = () => {
    return {
      getTextarea: () => screen.getByRole('textbox'),
      getStars: () => screen.getAllByRole('img'),
    };
  };

  const renderComponent = (overrides = {}) => {
    const { curUser, wrapper } = getLoggedInUser();

    const props = getProps(toPlainObject(curUser), overrides);

    render(<ActionReview {...props} />, { wrapper });

    return {
      form: () => getForm(),
      props,
      user: userEvent.setup(),
    };
  };

  it('should render create mode correctly', () => {
    renderComponent();

    expect(screen.getByText(/write a review/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /submit/i })).toBeInTheDocument();
  });

  it('should show validation error on empty submit', async () => {
    const { form, user } = renderComponent();

    const submitButton = screen.getByRole('button', { name: /submit/i });

    await user.type(form().getTextarea(), 'a');
    await user.clear(form().getTextarea());
    await user.click(submitButton);

    expect(
      await screen.findByText('Please make sure you fill out all fields and the review does not exceed 400 characters')
    ).toBeInTheDocument();
  });

  it('should submit successfully and navigate back (create)', async () => {
    const { user, form } = renderComponent();

    const submitButton = screen.getByRole('button', { name: /submit/i });

    await user.hover(form().getStars()[4]);
    await user.type(form().getTextarea(), 'Great reviewer!'), await user.click(submitButton);

    await waitFor(() => {
      expect(mockNavigate).toHaveBeenCalledWith(-1);
    });
  });

  it('should render edit mode and pre-fills data', async () => {
    renderComponent({ action: 'edit' });

    const updateButton = screen.getByRole('button', { name: /update/i });

    expect(screen.getByText('Edit review'));
    expect(updateButton).toBeInTheDocument();
  });

  it('should render update the review and navigate back', async () => {
    const { form, user } = renderComponent({ action: 'edit' });

    const updateButton = screen.getByRole('button', { name: /update/i });

    await user.type(form().getTextarea(), 'Updated review');
    await user.hover(form().getStars()[4]);
    await user.click(updateButton);

    await waitFor(() => {
      expect(mockNavigate).toHaveBeenCalledWith(-1);
    });
  });
});
