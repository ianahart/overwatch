import { render, screen } from '@testing-library/react';
import ReviewsCompletedLineChart from '../../../../../../src/components/Dashboard/Routes/Reviewer/Statistic/ReviewsCompletedLineChart';
import { IStatisticReviewsCompleted } from '../../../../../../src/interfaces';

const mockData: IStatisticReviewsCompleted[] = [
  { day: '2025-07-01', reviewsCompleted: 2 },
  { day: '2025-07-02', reviewsCompleted: 5 },
  { day: '2025-07-03', reviewsCompleted: 3 },
];

describe('ReviewsCompletedLineChart', () => {
  it('renders chart with title', () => {
    render(<ReviewsCompletedLineChart data={mockData} />);
    expect(screen.getByText('Reviews Completed This Month')).toBeInTheDocument();
  });
});
