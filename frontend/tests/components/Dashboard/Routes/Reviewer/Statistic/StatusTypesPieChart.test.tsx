import { render, screen } from '@testing-library/react';
import StatusTypesPieChart from '../../../../../../src/components/Dashboard/Routes/Reviewer/Statistic/StatusTypesPieChart';
import { IStatisticStatusTypes } from '../../../../../../src/interfaces';

const mockData: IStatisticStatusTypes[] = [
  { status: 'APPROVED', count: 4 },
  { status: 'CHANGES_REQUESTED', count: 2 },
  { status: 'COMMENTED', count: 1 },
];

describe('StatusTypesPieChart', () => {
  it('renders chart with title', () => {
    render(<StatusTypesPieChart data={mockData} />);
    expect(screen.getByText('Status of Reviews')).toBeInTheDocument();
  });
});
