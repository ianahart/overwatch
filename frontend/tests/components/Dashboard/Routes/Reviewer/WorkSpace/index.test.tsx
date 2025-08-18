// WorkSpaceContainer.test.tsx
import { render, screen, waitFor } from '@testing-library/react';
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { Mock } from 'vitest';
import WorkSpaceContainer from '../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace';
import { getLoggedInUser } from '../../../../../utils';

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return { ...actual, useDispatch: vi.fn(), useSelector: actual.useSelector };
});

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom');
  return { ...actual, useNavigate: vi.fn(), Outlet: vi.fn(() => <div data-testid="outlet" />) };
});

vi.mock('../../../../../state/store', async () => {
  const actual = await vi.importActual('../../../../../state/store');
  return {
    ...actual,
    useFetchLatestWorkspaceQuery: vi.fn(),
    useLazyFetchTodoListsQuery: vi.fn(),
  };
});

describe('WorkSpaceContainer', () => {
  const mockDispatch = vi.fn();
  const mockNavigate = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
    (useNavigate as Mock).mockReturnValue(mockNavigate);
  });

  const renderComponent = (overrides = {}) => {
    const { wrapper } = getLoggedInUser(
      { id: 1, slug: 'john' }, // user
      { workSpace: { workSpace: { id: 0, title: '', backgroundColor: '', userId: 1, createdAt: '' } } }
    );
    return render(<WorkSpaceContainer {...overrides} />, { wrapper });
  };

  it('renders CurrentWorkSpaces and WorkSpaceTitle', async () => {
    renderComponent();
    await waitFor(() => {
      expect(screen.getByTestId('outlet')).toBeInTheDocument();
    });
  });

  it('shows spinner when loading', () => {
    renderComponent();
    expect(screen.getByText(/loading/i)).toBeInTheDocument();
  });
});
