import { screen } from '@testing-library/react';
import { vi } from 'vitest';
import { AllProviders } from '../../AllProviders';
import { mockLocation, setMockParams } from '../../setup';

describe('EditReview', () => {
  const mockActionReview = vi.fn(() => <div data-testid="mock-action-review" />);

  beforeEach(async () => {
    vi.clearAllMocks();

    mockLocation.mockReturnValue({
      state: {
        authorId: 1,
        reviewerId: 2,
        avatarUrl: 'https://example.com/avatar.jpg',
        fullName: 'Test User',
      },
    });

    setMockParams({ reviewId: '42' });

    vi.doMock('../../../src/components/Review/ActionReview', () => ({
      __esModule: true,
      default: mockActionReview,
    }));

    const mod = await import('../../../src/components/Review/EditReview');
    const { render } = await import('@testing-library/react');
    render(<mod.default />, { wrapper: AllProviders });
  });

  it('should render ActionReview with correct props from useLocation and useParams', () => {
    expect(screen.getByTestId('mock-action-review')).toBeInTheDocument();

    expect(mockActionReview).toHaveBeenCalledWith(
      {
        action: 'edit',
        authorId: 1,
        reviewerId: 2,
        avatarUrl: 'https://example.com/avatar.jpg',
        fullName: 'Test User',
        reviewId: 42,
      },
      expect.anything()
    );
  });
});
