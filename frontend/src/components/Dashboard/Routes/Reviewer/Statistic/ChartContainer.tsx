interface IChartContainerProps {
  children: React.ReactNode;
  title: string;
}

const ChartContainer = ({ children, title }: IChartContainerProps) => {
  return (
    <div data-testid="ChartContainer" className="my-6">
      <h3 className="text-center text-2xl text-white">{title}</h3>
      <div style={{ width: '100%', height: '400px' }}>{children}</div>
    </div>
  );
};

export default ChartContainer;
