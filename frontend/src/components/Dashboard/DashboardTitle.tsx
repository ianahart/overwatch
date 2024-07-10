import logo from '../../assets/overwatch_logo.png';

export interface IDashboardTitleProps {
  title: string;
  version: number;
}

const DashboardTitle = ({ title, version }: IDashboardTitleProps) => {
  return (
    <div className="flex items-center justify-between">
      <div>
        <img className="rounded h-14 w-14" src={logo} alt="two big eyes that are green" />
      </div>
      <div className="">
        <h2 className="text-xl text-green-400 font-display tracking-wider">{title}</h2>
        <p className="font-bold">v {version}</p>
      </div>
    </div>
  );
};

export default DashboardTitle;
