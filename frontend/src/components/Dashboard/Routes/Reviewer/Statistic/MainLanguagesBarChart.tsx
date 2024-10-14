import { BarChart, Bar, Rectangle, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { IStatisticMainLanguage } from '../../../../../interfaces';
import ChartContainer from './ChartContainer';

export interface IMainLanguagesBarChartProps {
  data: IStatisticMainLanguage[];
}

const MainLanguagesBarChart = ({ data }: IMainLanguagesBarChartProps) => {
  console.log(data);
  return (
    <ChartContainer title="Most Reviewed Languages">
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
          <XAxis dataKey="language" tick={{ fill: 'white' }} />
          <YAxis />
          <Tooltip />
          <Legend />
          <Bar dataKey="count" fill="#4ade80" activeBar={<Rectangle fill="pink" stroke="blue" />} />
        </BarChart>
      </ResponsiveContainer>
    </ChartContainer>
  );
};

export default MainLanguagesBarChart;
