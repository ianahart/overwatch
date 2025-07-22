import { screen, render, waitFor } from '@testing-library/react';
import { Mock } from 'vitest';
import { useDispatch } from 'react-redux';
import { HttpResponse, http } from 'msw';

import { getLoggedInUser } from '../../../../../utils';
import Reviews from '../../../../../../src/components/Dashboard/Routes/Reviewer/Reviews';
import { mockNavigate } from '../../../../../setup';
import { clearRepositoryReviews } from '../../../../../../src/state/store';
import { server } from '../../../../../mocks/server';
import { baseURL } from '../../../../../../src/util';

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

describe('Reviews', () => {
  const mockDispatch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const getComponents = () => {
    return {
      getReviewsFilters: () => screen.getByTestId('ReviewsFilters'),
      getRepositoryReviewList: () => screen.getByTestId('RepositoryReviewList'),
    };
  };

  const renderComponent = () => {
    const { curUser, wrapper } = getLoggedInUser();

    const { unmount } = render(<Reviews />, { wrapper });

    return {
      components: getComponents(),
      unmount,
      curUser,
    };
  };

  it('should render filters and review list', () => {
    const { components } = renderComponent();

    const { getReviewsFilters, getRepositoryReviewList } = components;

    expect(getReviewsFilters()).toBeInTheDocument();
    expect(getRepositoryReviewList()).toBeInTheDocument();
  });

  it('should dispatch clearRepositoryReviews on unmount', () => {
    const { unmount } = renderComponent();
    unmount();

    expect(mockDispatch).toHaveBeenCalledWith(clearRepositoryReviews());
  });

  it('should navigate to billing if error is present', async () => {
    server.use(
      http.get(`${baseURL}/users/:userId/payment-methods`, () => {
        return HttpResponse.json(
          {
            message: 'No payment method found',
          },
          { status: 404 }
        );
      })
    );

    const { curUser } = renderComponent();

    await waitFor(() => {
      expect(mockNavigate).toHaveBeenCalledWith(`/settings/${curUser.slug}/billing?toast=show`);
    });
  });
});
