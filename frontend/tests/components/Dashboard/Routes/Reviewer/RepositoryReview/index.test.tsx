import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';
import RepositoryReview from '../../../../../../src/components/Dashboard/Routes/Reviewer/RepositoryReview';
import { getLoggedInUser } from '../../../../../utils';
import * as SessionService from '../../../../../../src/util/SessionService';
import { setMockParams } from '../../../../../setup';
import { ERepositoryView } from '../../../../../../src/enums';
import { db } from '../../../../../mocks/db';

vi.mock('../../../../../../src/components/Dashboard/Routes/Reviewer/RepositoryReview/FileTree', () => ({
  default: () => <div data-testid="FileTree" />,
}));

vi.mock('../../../../../../src/components/Dashboard/Routes/Reviewer/RepositoryReview/CodeViewer', () => ({
  default: () => <div data-testid="CodeViewer" />,
}));

vi.mock('../../../../../../src/components/Dashboard/Routes/Reviewer/RepositoryReview/RepositoryDetails', () => ({
  default: () => <div data-testid="RepositoryDetails" />,
}));

vi.mock('../../../../../../src/components/Dashboard/Routes/Reviewer/RepositoryReview/NavigationButton', () => ({
  default: ({ text }: { text: string }) => <button data-testid={`nav-${text.toLowerCase()}`}>{text}</button>,
}));

describe('RepositoryReview', () => {
  const mockGetItem = vi.spyOn(SessionService.Session, 'getItem');

  const originalLocation = window.location;
  beforeAll(() => {
    // @ts-ignore
    delete window.location;
    window.location = {
      ...originalLocation,
      assign: vi.fn(),
    };
  });

  afterAll(() => {
    window.location = originalLocation;
  });

  beforeEach(() => {
    setMockParams({ id: '123' });
    mockGetItem.mockReturnValue('123456');
    localStorage.clear();
  });

  const renderComponent = (stateOverrides = {}) => {
    const { wrapper } = getLoggedInUser(
      {},
      {
        repositoryTree: {
          repository: { ...toPlainObject(db.gitHubRepository.create()), reviewerId: 1, ownerId: 1 },
          repositoryNavView: ERepositoryView.DETAILS,
          repositoryLanguages: ['JavaScript'],
          repositoryPage: 1,
          ...stateOverrides,
        },
      }
    );
    render(<RepositoryReview />, { wrapper });
  };

  it('should render navigation buttons and FileTree', async () => {
    renderComponent();

    expect(await screen.findByTestId('nav-details')).toBeInTheDocument();
    expect(screen.getByTestId('nav-code')).toBeInTheDocument();
    expect(screen.getByTestId('FileTree')).toBeInTheDocument();
  });

  it('should show RepositoryDetails when repositoryNavView is DETAILS', async () => {
    renderComponent({ repositoryNavView: 'DETAILS' });

    expect(await screen.findByTestId('RepositoryDetails')).toBeInTheDocument();
    expect(screen.queryByTestId('CodeViewer')).not.toBeInTheDocument();
  });

  it('should show CodeViewer when repositoryNavView is CODE', async () => {
    renderComponent({ repositoryNavView: 'CODE' });

    expect(await screen.findByTestId('CodeViewer')).toBeInTheDocument();
    expect(screen.queryByTestId('RepositoryDetails')).not.toBeInTheDocument();
  });

  it('should fetch repository and populate localStorage', async () => {
    renderComponent();

    await waitFor(() => {
      expect(localStorage.getItem('content')).toBeDefined();
    });
  });

  it('should redirect to GitHub login if githubId is missing', async () => {
    mockGetItem.mockReturnValueOnce('');
    const locationAssign = vi.spyOn(window.location, 'assign').mockImplementation(() => {});

    renderComponent();

    await waitFor(() => {
      expect(locationAssign).toHaveBeenCalledWith(expect.stringContaining('https://github.com/login/oauth/authorize'));
    });

    locationAssign.mockRestore();
  });
});
