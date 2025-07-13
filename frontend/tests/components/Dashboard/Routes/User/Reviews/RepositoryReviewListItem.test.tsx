import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';

import RepositoryReviewListItem from '../../../../../../src/components/Dashboard/Routes/User/Reviews/RepositoryReviewListItem';
import { useDispatch } from 'react-redux';
import { Mock } from 'vitest';
import { getLoggedInUser } from '../../../../../utils';
import { IRepositoryReview, IUser } from '../../../../../../src/interfaces';
import { db } from '../../../../../mocks/db';
import userEvent from '@testing-library/user-event';

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

describe('RepositoryReviewListItem', () => {
  const mockDispatch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const getRepoDetails = (data: IRepositoryReview) => {
    const { firstName, lastName, language, repoName } = data;

    return {
      getFullName: () => screen.getByText(`${firstName} ${lastName}`),
      getAvatar: () => screen.getByRole('img', { name: repoName }),
      getLanguage: () => screen.getByText(language),
      getRepoName: () => screen.getByText(repoName),
    };
  };

  const getProps = (overrides: Partial<IRepositoryReview> = {}, ownerId: number) => {
    const data: IRepositoryReview = {
      ...toPlainObject(db.repository.create()),
      status: 'INPROGRESS',
      ...overrides,
      ownerId,
    };

    return { data };
  };

  const renderComponent = (
    userOverrides: Partial<IUser> = {},
    repositoryOverrides: Partial<IRepositoryReview> = {}
  ) => {
    const { curUser, wrapper } = getLoggedInUser({ ...userOverrides });

    const props = getProps(repositoryOverrides, curUser.id);
    const repoDetails = getRepoDetails(props.data);

    render(<RepositoryReviewListItem {...props} />, { wrapper });

    return {
      props,
      curUser,
      user: userEvent.setup(),
      repoDetails,
    };
  };

  it('should render avatar, name, language, and repo name', () => {
    const { repoDetails } = renderComponent();

    const { getAvatar, getLanguage, getRepoName, getFullName } = repoDetails;

    expect(getAvatar()).toBeInTheDocument();
    expect(getLanguage()).toBeInTheDocument();
    expect(getRepoName()).toBeInTheDocument();
    expect(getFullName()).toBeInTheDocument();
  });

  it('should show "Edit" and "Delete" icons for repository owner', () => {
    renderComponent({ role: 'USER' }, { ownerId: 1 });

    expect(screen.getByTestId('edit-repository-review-icon')).toBeInTheDocument();
    expect(screen.getByTestId('delete-repository-review-icon')).toBeInTheDocument();
  });

  it('should delete repository when trash icon is clicked', async () => {
    const { user } = renderComponent({ role: 'USER' }, { ownerId: 1 });

    await user.click(screen.getByTestId('delete-repository-review-icon'));

    await waitFor(() => {
      expect(mockDispatch).toHaveBeenCalled();
    });
  });

  it('should render feedback link for completed status', () => {
    renderComponent({ role: 'USER' }, { status: 'COMPLETED' });

    expect(screen.getByText(/your feedback/i)).toBeInTheDocument();
  });

  it('should render "Review Code" link for reviewer', async () => {
    renderComponent({ role: 'REVIEWER' }, { reviewerId: 1 });

    expect(screen.getByText(/review code/i)).toBeInTheDocument();
  });
});
