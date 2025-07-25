import { render, screen } from '@testing-library/react';
import TopRequesterHorizontalBarChart from '../../../../../../src/components/Dashboard/Routes/Reviewer/Statistic/TopRequesterHorizontalBarChart';
import { IStatisticTopRequesters } from '../../../../../../src/interfaces';

const mockData: IStatisticTopRequesters[] = [
  { fullName: 'Alice Johnson', count: 7 },
  { fullName: 'Bob Smith', count: 5 },
  { fullName: 'Carol Lee', count: 3 },
];

describe('TopRequesterHorizontalBarChart', () => {
  it('renders chart with title', () => {
    render(<TopRequesterHorizontalBarChart data={mockData} />);
    expect(screen.getByText('Top Requesters')).toBeInTheDocument();
  });
});
