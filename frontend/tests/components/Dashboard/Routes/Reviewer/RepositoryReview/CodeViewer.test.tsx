import { render, waitFor } from '@testing-library/react';
import { Mock } from 'vitest';
import { toPlainObject } from 'lodash';
import { useDispatch } from 'react-redux';
import userEvent from '@testing-library/user-event';

import CodeViewer from '../../../../../../src/components/Dashboard/Routes/Reviewer/RepositoryReview/CodeViewer';
import { getLoggedInUser } from '../../../../../utils';
import { IGitHubRepository } from '../../../../../../src/interfaces';
import { db } from '../../../../../mocks/db';
import { setRepositoryPage } from '../../../../../../src/state/store';

vi.mock('dompurify', () => ({
  default: {
    sanitize: vi.fn((html) => html),
  },
}));

vi.mock('shiki', () => ({
  codeToHtml: vi.fn(async () => '<span class="token">highlighted</span>'),
}));

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

describe('CodeViewer', () => {
  const mockDispatch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const renderComponent = () => {
    const repository: IGitHubRepository = { ...toPlainObject(db.gitHubRepository.create()), reviewerId: 2, ownerId: 1 };
    const repositoryPage = 0;
    const repositoryFile = { path: 'some/path/to/file.js', content: 'console.log("Hello")', language: 'js' };

    const { wrapper } = getLoggedInUser(
      {},
      {
        repositoryTree: {
          repository,
          repositoryFile,
          repositoryPage,
        },
      }
    );

    const { container } = render(<CodeViewer />, { wrapper });

    return {
      user: userEvent.setup(),
      repository,
      repositoryFile,
      repositoryPage,
      container,
    };
  };

  it('should render highlighted code into pre tag', async () => {
    const { container } = renderComponent();

    await waitFor(() => {
      const pre = container.querySelector('pre');
      expect(pre?.innerHTML).toContain('highlighted');
    });
  });

  it('should dispatch search on click with text content', async () => {
    const { container, user, repositoryPage } = renderComponent();

    const pre = container.querySelector('pre')!;
    pre.textContent = 'clickedWord';

    user.click(pre);

    await waitFor(() => {
      expect(mockDispatch).toHaveBeenCalledWith(setRepositoryPage(repositoryPage + 1));
    });
  });
  it('should dispatch search on text selection', async () => {
    const { container } = renderComponent();

    const pre = container.querySelector('pre')!;
    const textNode = document.createTextNode('highlighted');
    pre.appendChild(textNode);
    document.body.appendChild(pre);

    const range = document.createRange();
    range.selectNodeContents(textNode);
    const selection = window.getSelection();
    selection?.removeAllRanges();
    selection?.addRange(range);

    vi.useFakeTimers();

    pre.dispatchEvent(new MouseEvent('mouseup', { bubbles: true }));

    vi.advanceTimersByTime(350);
    vi.useRealTimers();

    await waitFor(() => {
      expect(mockDispatch).toHaveBeenCalledWith(
        expect.objectContaining({
          type: 'repositoryTree/setInitialRepositoryTree',
          payload: expect.any(Array),
        })
      );
    });
  });
});
