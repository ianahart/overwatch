import { render, screen } from '@testing-library/react';
import ReviewTypesCompletedDoughnutChart from '../../../../../../src/components/Dashboard/Routes/Reviewer/Statistic/ReviewTypesCompletedDoughnutChart';
import { IStatisticReviewTypesCompleted } from '../../../../../../src/interfaces';

const mockData: IStatisticReviewTypesCompleted[] = [
  { reviewType: 'Approved', count: 10 },
  { reviewType: 'Changes Requested', count: 5 },
  { reviewType: 'Commented', count: 3 },
  { reviewType: 'Dismissed', count: 2 },
];

describe('ReviewTypesCompletedDoughnutChart', () => {
  it('renders chart with title', () => {
    render(<ReviewTypesCompletedDoughnutChart data={mockData} />);
    expect(screen.getByText('Review Types Completed')).toBeInTheDocument();
  });
});
