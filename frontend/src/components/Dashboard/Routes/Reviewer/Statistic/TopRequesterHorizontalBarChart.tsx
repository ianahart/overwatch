import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { IStatisticTopRequesters } from '../../../../../interfaces';
import ChartContainer from './ChartContainer';

export interface ITopRequesterHorizontalBarChartProps {
  data: IStatisticTopRequesters[];
}

const TopRequesterHorizontalBarChart = ({ data }: ITopRequesterHorizontalBarChartProps) => {
  return (
    <ChartContainer title="Top Requesters">
      <ResponsiveContainer width="100%" height="100%">
        <BarChart
          data={data}
          layout="vertical"
          margin={{
            top: 5,
            right: 30,
            left: 20,
            bottom: 5,
          }}
        >
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis type="number" />
          <YAxis type="category" dataKey="fullName" />
          <Tooltip />
          <Legend />
          <Bar dataKey="count" fill="#4ade80" />
        </BarChart>
      </ResponsiveContainer>
    </ChartContainer>
  );
};

export default TopRequesterHorizontalBarChart;
