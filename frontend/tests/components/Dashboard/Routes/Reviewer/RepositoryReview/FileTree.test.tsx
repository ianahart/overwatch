import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';
import userEvent from '@testing-library/user-event';
import { setMockParams } from '../../../../../setup';

import FileTree from '../../../../../../src/components/Dashboard/Routes/Reviewer/RepositoryReview/FileTree';
import { IGitHubRepository, IGitHubTree } from '../../../../../../src/interfaces';
import { ERepositoryView } from '../../../../../../src/enums';
import { db } from '../../../../../mocks/db';
import { getLoggedInUser } from '../../../../../utils';
import { useDispatch } from 'react-redux';
import { Mock } from 'vitest';

vi.mock('../../../../../../src/util/SessionService', () => ({
  Session: {
    getItem: vi.fn(() => '12345'),
  },
}));

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

describe('FileTree', () => {
  const mockDispatch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    setMockParams({ id: '1' });

    (useDispatch as Mock).mockReturnValue(mockDispatch);
    vi.spyOn(window, 'scrollTo').mockImplementation(() => {});
  });

  const getRepositoryState = (overrides = {}) => {
    const repository: IGitHubRepository = { ...toPlainObject(db.gitHubRepository.create()), ownerId: 1, reviewerId: 1 };
    const repositoryPage = 0;
    const searchingCodeQuery = 'console.log("Hello")';
    const repositoryNavView = ERepositoryView.CODE;
    const repositoryTree: IGitHubTree[] = [
      {
        path: 'README.md',
        sha: '3b18e8a9c0b0a6f6d3fa738d3f4ea7bd829d4e9d',
        size: 1200,
        type: 'blob',
        url: 'https://api.github.com/repos/example/repo/git/blobs/3b18e8a9c0b0a6f6d3fa738d3f4ea7bd829d4e9d',
      },
      {
        path: 'src/index.ts',
        sha: '5e6a2a0f9a7c62228dbab5b8e9b4fc0a4d1be30c',
        size: 580,
        type: 'blob',
        url: 'https://api.github.com/repos/example/repo/git/blobs/5e6a2a0f9a7c62228dbab5b8e9b4fc0a4d1be30c',
      },
    ];

    return {
      repository,
      repositoryPage,
      repositoryTree,
      searchingCodeQuery,
      repositoryNavView,
      ...overrides,
    };
  };

  const renderComponent = () => {
    const repositoryState = getRepositoryState();

    const { wrapper } = getLoggedInUser(
      {},
      {
        repositoryTree: { ...repositoryState },
      }
    );

    render(<FileTree />, { wrapper });

    return {
      repositoryState,
      user: userEvent.setup(),
    };
  };

  it('should render files and search input', () => {
    renderComponent();
    expect(screen.getByPlaceholderText(/search files/i)).toBeInTheDocument();
    expect(screen.getByText('src/index.ts')).toBeInTheDocument();
  });

  it('should load more files when clicking "More files..." button', async () => {
    const { user } = renderComponent();

    const moreFilesBtn = screen.getByRole('button', { name: /more files/i });
    await user.click(moreFilesBtn);

    await waitFor(() => {
      expect(mockDispatch).toHaveBeenCalledWith(expect.objectContaining({ type: 'repositoryTree/setRepositoryPage' }));
    });
  });
});
