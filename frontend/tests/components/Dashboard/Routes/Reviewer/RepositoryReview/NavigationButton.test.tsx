import { screen, render, waitFor } from '@testing-library/react';

import NavigationButton, {
  INavigationButtonProps,
} from '../../../../../../src/components/Dashboard/Routes/Reviewer/RepositoryReview/NavigationButton';
import { Mock } from 'vitest';
import { useDispatch } from 'react-redux';
import { ERepositoryView } from '../../../../../../src/enums';
import { getLoggedInUser } from '../../../../../utils';
import userEvent from '@testing-library/user-event';

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

describe('NavigationButton', () => {
  const mockDispatch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const getProps = () => {
    const props: INavigationButtonProps = {
      text: 'Code View',
      keyword: ERepositoryView.CODE,
      icon: <span data-testid="icon">📄</span>,
    };
    return props;
  };

  const renderComponent = (overrides = {}) => {
    const props = getProps();

    const { wrapper } = getLoggedInUser(
      {},
      {
        repositoryTree: {
          repositoryNavView: ERepositoryView.DETAILS,
          ...overrides,
        },
      }
    );

    render(<NavigationButton {...props} />, { wrapper });

    return {
      props,
      user: userEvent.setup(),
      getButton: () => screen.getByTestId('NavigationButton'),
    };
  };

  it('should render correctly with default styling', () => {
    const { getButton } = renderComponent();

    const button = getButton();

    expect(button).toBeInTheDocument();
    expect(button).toHaveClass('bg-gray-950');
    expect(screen.queryByText(/code view/i)).not.toBeInTheDocument();
  });

  it('should show tooltip on hover', async () => {
    const { user, getButton } = renderComponent();

    await user.hover(getButton());

    expect(await screen.findByText(/code view/i)).toBeInTheDocument();

    await user.unhover(getButton());

    await waitFor(() => {
      expect(screen.queryByText(/code view/i)).not.toBeInTheDocument();
    });
  });

  it('should apply active styling', async () => {
    const { getButton } = renderComponent({ repositoryNavView: ERepositoryView.CODE });

    const button = getButton();
    expect(button).toHaveClass('bg-green-400');
  });
});
