import { screen, render } from '@testing-library/react';
import { getLoggedInUser } from '../../../../../utils';
import RepositoryReviewList from '../../../../../../src/components/Dashboard/Routes/User/Reviews/RepositoryReviewList';
import userEvent from '@testing-library/user-event';
import { repositoryPaginationState } from '../../../../../../src/data';
import { IRepositoryReview } from '../../../../../../src/interfaces';
import { createRepositories } from '../../../../../mocks/dbActions';

vi.mock('../../../../../../src/util', () => ({
  retrieveTokens: () => ({ token: 'mock-token' }),
}));

describe('RepositoryReviewList', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getRepositoryReviewsState = () => {
    const repositoryReviews: IRepositoryReview[] = createRepositories(10, { status: 'COMPLETED' });
    return {
      sortFilter: 'desc',
      statusFilter: 'INCOMPLETE',
      languageFilter: 'All',
      pagination: { ...repositoryPaginationState, page: 1, totalPages: 3 },
      repositoryReviews,
    };
  };

  const renderComponent = () => {
    const repositoryReviewsState = getRepositoryReviewsState();

    const { wrapper } = getLoggedInUser(
      {},
      {
        repositoryReviews: { ...repositoryReviewsState },
      }
    );

    render(<RepositoryReviewList />, { wrapper });

    return {
      repositoryReviewsState,
      user: userEvent.setup(),
    };
  };

  it('should render the repository review list items', async () => {
    const { repositoryReviewsState } = renderComponent();

    const repositoryReviewListItems = await screen.findAllByTestId('RepositoryReviewListItem');

    expect(repositoryReviewListItems.length).toBe(repositoryReviewsState.repositoryReviews.length);
  });

  it('should render pagination controls when on middle page', async () => {
    renderComponent();

    expect(await screen.findByRole('button', { name: /next/i })).toBeInTheDocument();
    expect(await screen.findByRole('button', { name: /prev/i })).toBeInTheDocument();
    expect(await screen.findByText('2')).toBeInTheDocument();
  });

  it('should call "fetchRepositories" when clicking "Next" button', async () => {
    const { user } = renderComponent();

    await user.click(await screen.findByRole('button', { name: /next/i }));

    expect(await screen.findByText('3')).toBeInTheDocument();
  });

  it('should call fetchRepositories when clicking the "Prev" button', async () => {
    const { user } = renderComponent();

    await user.click(await screen.findByRole('button', { name: /prev/i }));

    expect(await screen.findByText('1')).toBeInTheDocument();
  });
});
