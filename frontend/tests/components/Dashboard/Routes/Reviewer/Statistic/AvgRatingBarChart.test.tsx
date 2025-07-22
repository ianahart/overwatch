import { screen, render } from '@testing-library/react';
import { toPlainObject } from 'lodash';

import AvgRatingBarChart from '../../../../../../src/components/Dashboard/Routes/Reviewer/Statistic/AvgRatingBarChart';
import { IStatisticAvgRating } from '../../../../../../src/interfaces';
import { AllProviders } from '../../../../../AllProviders';
import { db } from '../../../../../mocks/db';

vi.mock('recharts', async (importOriginal) => {
  const original = await importOriginal<typeof import('recharts')>();

  return {
    ...original,
    ResponsiveContainer: ({ children }: any) => <div style={{ width: 800, height: 400 }}>{children}</div>,
  };
});

describe('AvgRatingBarChart', () => {
  const getData = (count: number) => {
    const data: IStatisticAvgRating[] = [];

    for (let i = 0; i < count; i++) {
      const value: IStatisticAvgRating = { ...toPlainObject(db.statisticAvgRating.create()) };

      data.push(value);
    }
    return data;
  };

  const renderComponent = () => {
    const data = getData(5);

    render(<AvgRatingBarChart data={data} />, { wrapper: AllProviders });

    return { data };
  };

  it('should render the chart inside ChartContainer with correct title', () => {
    renderComponent();

    expect(screen.getByTestId('ChartContainer')).toBeInTheDocument();
    expect(screen.getByText(/average review ratings/i)).toBeInTheDocument();
  });

  it('should render all bars with correct labels from data', () => {
    const { data } = renderComponent();

    const textNodes = Array.from(document.querySelectorAll('text')).map((el) => el.textContent?.trim());

    data.forEach(({ name }) => {
      expect(textNodes).toContain(name);
    });
  });
});
