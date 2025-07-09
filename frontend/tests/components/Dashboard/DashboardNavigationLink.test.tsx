import { screen, render, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { useDispatch } from 'react-redux';
import { Mock } from 'vitest';

import DashboardNavigationLink from '../../../src/components/Dashboard/DashboardNavigationLink';
import { AllProviders } from '../../AllProviders';
import { clearChat } from '../../../src/state/store';

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

describe('DashboardNavigationLink', () => {
  const mockDispatch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const getProps = (overrides = {}) => {
    return {
      path: '/test-path',
      label: 'Test Path',
      icon: <span data-testid="icon">Icon</span>,
      ...overrides,
    };
  };

  const renderComponent = (overrides = {}) => {
    const props = getProps(overrides);

    render(<DashboardNavigationLink {...props} />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      props,
    };
  };

  it('should render link with correct label and path', () => {
    const { props } = renderComponent();

    const { path, label } = props;

    const link = screen.getByRole('link', { name: label });

    expect(link).toHaveAttribute('href', path);
  });

  it('should render the icon', () => {
    renderComponent();
    expect(screen.getByTestId('icon')).toBeInTheDocument();
  });

  it('should call "clearChat" when path includes /connects', async () => {
    const { user, props } = renderComponent({ path: '/dashboard/connects' });

    await user.click(screen.getByRole('link', { name: props.label }));

    await waitFor(() => {
      expect(mockDispatch).toHaveBeenCalledWith(clearChat());
    });
  });

  it('should not call "clearChat" when link does NOT include "/connects"', async () => {
    const { user, props } = renderComponent();

    await user.click(screen.getByRole('link', { name: props.label }));

    await waitFor(() => {
      expect(mockDispatch).not.toHaveBeenCalled();
    });
  });
});
