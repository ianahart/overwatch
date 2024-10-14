import { BarChart, Bar, Rectangle, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { IStatisticAvgRating } from '../../../../../interfaces';
import ChartContainer from './ChartContainer';

export interface IAvgRatingBarChartProps {
  data: IStatisticAvgRating[];
}

const AvgRatingBarChart = ({ data }: IAvgRatingBarChartProps) => {
  return (
    <ChartContainer title="Average Review Ratings">
      <ResponsiveContainer width="100%" height="100%">
        <BarChart
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
          <XAxis dataKey="name" tick={{ fill: 'white' }} />
          <YAxis />
          <Tooltip />
          <Legend />
          <Bar dataKey="average" fill="#4ade80" activeBar={<Rectangle fill="pink" stroke="blue" />} />
        </BarChart>
      </ResponsiveContainer>
    </ChartContainer>
  );
};

export default AvgRatingBarChart;
