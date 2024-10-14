import { PieChart, Pie, Cell, ResponsiveContainer } from 'recharts';
import { IStatisticReviewTypesCompleted } from '../../../../../interfaces';
import ChartContainer from './ChartContainer';

export interface IReviewTypesCompletedDoughnutChartProps {
  data: IStatisticReviewTypesCompleted[];
}

export interface ILabelProps {
  cx: number;
  cy: number;
  midAngle: number;
  innerRadius: number;
  outerRadius: number;
  percent: number;
  index: number;
}

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042'];

const RADIAN = Math.PI / 180;
const renderCustomizedLabel = (
  { cx, cy, midAngle, innerRadius, outerRadius, percent, index }: ILabelProps,
  data: IStatisticReviewTypesCompleted[]
) => {
  const radius = innerRadius + (outerRadius - innerRadius) * 0.5;
  const x = cx + radius * Math.cos(-midAngle * RADIAN);
  const y = cy + radius * Math.sin(-midAngle * RADIAN);

  const reviewType = data[index].reviewType.toLowerCase();
  return (
    <text x={x} y={y} fill="white" textAnchor={x > cx ? 'start' : 'end'} dominantBaseline="central">
      {`${reviewType}: ${(percent * 100).toFixed(0)}%`}
    </text>
  );
};
const ReviewTypesCompletedDoughnutChart = ({ data }: IReviewTypesCompletedDoughnutChartProps) => {
  return (
    <ChartContainer title="Review Types Completed">
      <ResponsiveContainer width="100%" height="100%">
        <PieChart width={400} height={400}>
          <Pie
            data={data}
            cx="50%"
            cy="50%"
            labelLine={false}
            label={(props) => renderCustomizedLabel(props, data)}
            outerRadius={80}
            fill="#8884d8"
            dataKey="count"
          >
            {data.map((_, index) => (
              <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
            ))}
          </Pie>
        </PieChart>
      </ResponsiveContainer>
    </ChartContainer>
  );
};

export default ReviewTypesCompletedDoughnutChart;
