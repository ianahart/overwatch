import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import ChartContainer from './ChartContainer';
import { IStatisticAvgReviewTime } from '../../../../../interfaces';

export interface IAvgReviewTimeLineChartProps {
  data: IStatisticAvgReviewTime[];
}

const AvgReviewTimeLineChart = ({ data }: IAvgReviewTimeLineChartProps) => {
  return (
    <ChartContainer title="Average Review Times This Month">
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
          <XAxis dataKey="month" tick={{ fill: 'white' }} />
          <YAxis />
          <Tooltip />
          <Legend />
          <Line type="monotone" dataKey="avgReviewTime" name="minutes" stroke="#4ade80" />
        </LineChart>
      </ResponsiveContainer>
    </ChartContainer>
  );
};

export default AvgReviewTimeLineChart;
