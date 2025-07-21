import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';

import { db } from '../../../../../mocks/db';
import { getLoggedInUser } from '../../../../../utils';
import { IGitHubRepository } from '../../../../../../src/interfaces';
import RepositoryDetails from '../../../../../../src/components/Dashboard/Routes/Reviewer/RepositoryReview/RepositoryDetails';
import userEvent from '@testing-library/user-event';
import { updateReviewStartTimeSpy } from '../../../../../handlers/repositories';

export interface IOverrides {
  repository: Partial<IGitHubRepository>;
  repositoryLanguages: string[];
}

describe('RepositoryDetails', () => {
  const getRepositoryState = (overrides: IOverrides = { repository: {}, repositoryLanguages: [] }) => {
    const repository: IGitHubRepository = {
      ...toPlainObject(db.gitHubRepository.create()),
      reviewerId: 1,
      ownerId: 1,
      reviewType: 'BUG',
      status: 'INCOMPLETE',
      ...overrides.repository,
    };

    return { repository, repositoryLanguages: [...overrides.repositoryLanguages] };
  };

  const renderComponent = (overrides: IOverrides = { repository: {}, repositoryLanguages: [] }) => {
    const repositoryState = getRepositoryState(overrides);

    const { wrapper } = getLoggedInUser(
      {},
      {
        repositoryTree: {
          ...repositoryState,
        },
      }
    );

    render(<RepositoryDetails />, { wrapper });

    return {
      repositoryState,
      user: userEvent.setup(),
    };
  };

  it('should render repo name, avatar, and link', () => {
    const { repositoryState } = renderComponent();

    const { repoName, avatarUrl, repoUrl } = repositoryState.repository;

    expect(screen.getByRole('heading', { name: repoName, level: 2 })).toBeInTheDocument();
    const avatar = screen.getByRole('img');
    const link = screen.getByRole('link');
    expect(avatar).toHaveAttribute('src', avatarUrl);
    expect(link).toHaveAttribute('href', repoUrl);
  });

  it('should render status and review type badges', () => {
    renderComponent();

    expect(screen.getByText('Incomplete')).toBeInTheDocument();
    expect(screen.getByText('Bug-fixes')).toBeInTheDocument();
  });

  it('should render main language and omits others if only one', () => {
    const { repositoryState } = renderComponent({
      repository: {},
      repositoryLanguages: ['JavaScript'],
    });

    expect(screen.getByText('Main language is')).toBeInTheDocument();
    expect(screen.getByText(repositoryState.repository.language)).toBeInTheDocument();
    expect(screen.getByText('No other languages')).toBeInTheDocument();
  });

  it('should render other languages if present', () => {
    const { repositoryState } = renderComponent({ repository: {}, repositoryLanguages: ['JavaScript', 'HTML', 'CSS'] });

    const { repositoryLanguages } = repositoryState;

    expect(screen.queryByText('No other languages')).not.toBeInTheDocument();

    repositoryLanguages.forEach((language) => {
      expect(screen.getByText(language)).toBeInTheDocument();
    });
  });

  it('should show fallback when no comment is provided', () => {
    renderComponent({ repository: { comment: '' }, repositoryLanguages: ['JavaScript'] });
    expect(screen.getByText('The user did not leave any comments for you.')).toBeInTheDocument();
  });

  it('should render user comment if provided', () => {
    renderComponent({ repository: { comment: 'review thoroughly' }, repositoryLanguages: ['JavaScript'] });
    expect(screen.getByText('review thoroughly')).toBeInTheDocument();
  });

  it('should render ReviewEditor section', () => {
    renderComponent();
    expect(screen.getByText(/Your feedback for the repository/i)).toBeInTheDocument();
  });

  it('should call updateRepositoryReviewStartTime', async () => {
    renderComponent({
      repository: { status: 'INCOMPLETE', reviewStartTime: null! },
      repositoryLanguages: ['JavaScript'],
    });
    await waitFor(() => {
      expect(updateReviewStartTimeSpy).toHaveBeenCalled();
    });
  });
});
