import { screen, render } from '@testing-library/react';
import { vi } from 'vitest';
import { mockLocation } from '../../setup';
import { AllProviders } from '../../AllProviders';

describe('CreateReview', () => {
  const mockActionReview = vi.fn(() => <div data-testid="mock-action-review" />);

  beforeEach(async () => {
    vi.clearAllMocks();

    const mockState = {
      authorId: 1,
      reviewerId: 2,
      avatarUrl: 'https://example.com/avatar.jpg',
      fullName: 'Test User',
    };

    mockLocation.mockReturnValue({ state: mockState });

    vi.doMock('../../../src/components/Review/ActionReview', () => ({
      __esModule: true,
      default: mockActionReview,
    }));

    const mod = await import('../../../src/components/Review/CreateReview');
    render(<mod.default />, { wrapper: AllProviders });
  });

  it('should render ActionReview with correct props from location.state', () => {
    expect(screen.getByTestId('mock-action-review')).toBeInTheDocument();

    expect(mockActionReview).toHaveBeenCalledWith(
      {
        action: 'create',
        authorId: 1,
        reviewerId: 2,
        avatarUrl: 'https://example.com/avatar.jpg',
        fullName: 'Test User',
      },
      expect.anything()
    );
  });
});
