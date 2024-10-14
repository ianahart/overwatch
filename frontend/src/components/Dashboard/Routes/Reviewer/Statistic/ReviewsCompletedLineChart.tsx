import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import ChartContainer from './ChartContainer';
import { IStatisticReviewsCompleted } from '../../../../../interfaces';

export interface IAvgReviewTimeLineChartProps {
  data: IStatisticReviewsCompleted[];
}

const ReviewsCompletedLineChart = ({ data }: IAvgReviewTimeLineChartProps) => {
  return (
    <ChartContainer title="Reviews Completed This Month">
      <ResponsiveContainer width="100%" height="100%">
        <LineChart
          width={500}
          height={300}
          data={data}
          margin={{
            top: 5,
            right: 30,
            left: 20,
            bottom: 5,
          }}
        >
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="day" tick={{ fill: 'white' }} />
          <YAxis />
          <Tooltip />
          <Legend />
          <Line type="monotone" dataKey="reviewsCompleted" name="Number of reviews" stroke="#4ade80" />
        </LineChart>
      </ResponsiveContainer>
    </ChartContainer>
  );
};

export default ReviewsCompletedLineChart;
