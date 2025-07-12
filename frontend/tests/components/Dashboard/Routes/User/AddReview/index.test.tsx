import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';

import AddReview from '../../../../../../src/components/Dashboard/Routes/User/AddReview';
import { useDispatch } from 'react-redux';
import { Mock } from 'vitest';
import { getLoggedInUser } from '../../../../../utils';
import { db } from '../../../../../mocks/db';
import { IConnection } from '../../../../../../src/interfaces';
import userEvent from '@testing-library/user-event';
import { Session } from '../../../../../../src/util/SessionService';

vi.mock('../../../../../../src/util/SessionService', () => ({
  Session: {
    getItem: vi.fn(() => 'fake-github-token'),
    setItem: vi.fn(),
  },
}));

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

vi.mock('../../../../../../src/util', async (importOriginal) => {
  const actual = await importOriginal<typeof import('../../../../../../src/util')>();
  return {
    ...actual,
    retrieveTokens: () => ({
      token: 'mock-token',
      refreshToken: 'mock-refresh-token',
    }),
  };
});

describe('AddReview', () => {
  const mockDispatch = vi.fn();

  beforeEach(() => {
    localStorage.clear();
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const createReviewers = (ids: number[]) => {
    return ids.map((id) => {
      return { ...toPlainObject(db.connection.create()), receiverId: id, senderId: 1 };
    });
  };

  const renderComponent = (overrides = {}) => {
    const selectedReviewer: IConnection = {
      ...toPlainObject(db.connection.create()),
      receiverId: 2,
      senderId: 1,
      ...overrides,
    };
    const reviewers: IConnection[] = createReviewers([3, 4]);

    localStorage.setItem('selected_reviewer', String(selectedReviewer.receiverId));

    const { wrapper } = getLoggedInUser(
      {},
      {
        addReview: {
          selectedReviewer,
          reviewers,
        },
      }
    );

    render(<AddReview />, { wrapper });

    return {
      reviewers,
      selectedReviewer,
      user: userEvent.setup(),
    };
  };

  it('should render GitHub login when no github_access_token exists', () => {
    (Session.getItem as Mock).mockReturnValue('');

    renderComponent({ id: 0 });

    screen.debug();
    expect(screen.getByText(/please sign in with GitHub/i)).toBeInTheDocument();
  });

  it('should render "ChosenReviewer" and reviewers when authenticated', async () => {
    (Session.getItem as Mock).mockReturnValue('fake-github-token');

    renderComponent();

    expect(await screen.findByText(/select a connection/i)).toBeInTheDocument();
    expect(screen.getByText(/click on a repository/i)).toBeInTheDocument();
  });

  it('should paginate reviewers when "Load more..." is clicked', async () => {
    (Session.getItem as Mock).mockReturnValue('fake-github-token');

    const { user } = renderComponent();

    const loadMoreBtn = await screen.findByRole('button', { name: /load more/i });

    await user.click(loadMoreBtn);

    expect(screen.getByText(/load more/i)).toBeInTheDocument();
  });

  it('should automatically select reviewer from localStorage if present', async () => {
    renderComponent();

    await waitFor(() => {
      expect(screen.getByText(/you have selected/i)).toBeInTheDocument();
    });
  });
});
