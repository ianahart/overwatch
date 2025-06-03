import { screen, render } from '@testing-library/react';

import { db } from '../../mocks/db';
import { getWrapper } from '../../RenderWithProviders';
import userEvent from '@testing-library/user-event';
import { toPlainObject } from 'lodash';
import ReviewerTestWrapper from './ReviewerTestWrapper';
import { mockNavigate } from '../../setup';

describe('Reviewer', () => {
  beforeEach(() => {
    db.minProfile.deleteMany({ where: { id: { equals: 1 } } });
    db.user.deleteMany({ where: { id: { equals: 1 } } });
    db.availability.deleteMany({ where: { id: { equals: 1 } } });
    db.slot.deleteMany({ where: { id: { equals: 1 } } });
    db.compatibleProgrammingLanguage.deleteMany({ where: { id: { equals: 1 } } });
  });

  const renderComponent = (overrides = {}) => {
    const user = {
      ...db.user.create(),
      id: 1,
      ...overrides,
      token: db.token.create().token,
    };

    const wrapper = getWrapper({
      user: {
        user,
        token: user.token,
      },
    } as any);

    const availability = db.availability.create({
      id: 1,
      day: 'Monday',
      slots: [db.slot.create({ id: 1, startTime: new Date().toString(), endTime: new Date().toString() })],
    });

    const programmingLanguage = db.compatibleProgrammingLanguage.create({
      id: 1,
      name: 'JavaScript',
      isCompatible: true,
    });

    const initialReviewer = toPlainObject(
      db.minProfile.create({
        id: 1,
        fullName: 'John Doe',
        country: 'USA',
        reviewAvgRating: 4,
        numOfReviews: 10,
        isFavorited: false,
        lastActive: new Date().toISOString(),
        lastActiveReadable: 'Yesterday',
        availability: [availability],
        programmingLanguages: [programmingLanguage],
      })
    );

    render(<ReviewerTestWrapper initialReviewer={initialReviewer} />, { wrapper });

    return {
      user: userEvent.setup(),
      getFavoriteIcon: () => screen.findByTestId('favorite-reviewer'),
      getViewButton: () => screen.getByRole('button', { name: /view/i }),
    };
  };

  it('should render reviewer details correctly', () => {
    renderComponent();

    expect(screen.getByText('John Doe')).toBeInTheDocument();
    expect(screen.getByText('USA')).toBeInTheDocument();
    expect(screen.getByText('JavaScript')).toBeInTheDocument();
    expect(screen.getByText(/number of reviews:/i)).toBeInTheDocument();
    expect(screen.getByText('Yesterday')).toBeInTheDocument();
  });

  it('renders the heart icon with the correct class when not favorited', async () => {
    const { getFavoriteIcon } = renderComponent();

    const favoriteIcon = await getFavoriteIcon();

    expect(favoriteIcon).toHaveClass('text-gray-400');
  });

  it('should toggle favorite icon when clicked', async () => {
    const { user, getFavoriteIcon } = renderComponent();

    const favoriteIcon = await getFavoriteIcon();

    expect(favoriteIcon).toHaveClass('text-gray-400');

    await user.click(favoriteIcon);

    const updatedFavoriteIcon = await getFavoriteIcon();
    expect(updatedFavoriteIcon).toHaveClass('text-red-400');
  });

  it('should navigate to the profile when button is clicked', async () => {
    const { user, getViewButton } = renderComponent();

    await user.click(getViewButton());

    expect(mockNavigate).toHaveBeenCalledWith(`/profiles/1`);
  });
});
