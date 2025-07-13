import { screen, render } from '@testing-library/react';

import ReviewsFilters from '../../../../../../src/components/Dashboard/Routes/User/Reviews/ReviewsFilters';
import { getLoggedInUser } from '../../../../../utils';
import userEvent from '@testing-library/user-event';
import { useDispatch } from 'react-redux';
import { Mock } from 'vitest';

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

vi.mock('../../../../../../src/util', async () => {
  return {
    retrieveTokens: vi.fn(() => ({ token: 'mock-token' })),
  };
});

describe('ReviewsFilters', () => {
  const mockDispatch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const getFilters = () => {
    return {
      sortFilter: 'desc',
      statusFilter: 'INCOMPLETE',
      languageFilter: 'All',
    };
  };

  const renderComponent = () => {
    const filters = getFilters();
    const { wrapper } = getLoggedInUser(
      {},
      {
        repositoryReviews: { ...filters },
      }
    );

    render(<ReviewsFilters />, { wrapper });

    return {
      user: userEvent.setup(),
      filters,
    };
  };

  it('should render sort, status, and language filters with correct values', async () => {
    renderComponent();
    const selects = await screen.findAllByRole('combobox');
    const sortSelect = selects[0];
    const statusSelect = selects[1];
    const languageSelect = selects[2];

    expect(sortSelect).toBeInTheDocument();
    expect(statusSelect).toBeInTheDocument();
    expect(languageSelect).toBeInTheDocument();
  });
});
