import DashboardNavigationLink from '../../DashboardNavigationLink';

export interface INavigationBlockProps {
  links: { path: string; label: string; id: number; icon: React.ReactNode }[];
  title: string;
}

const NavigationBlock = ({ links, title }: INavigationBlockProps) => {
  return (
    <div className="my-8">
      <h3 className="text-gray-400">{title}</h3>
      <ul>
        {links.map(({ path, label, id, icon }) => {
          return <DashboardNavigationLink key={id} path={path} label={label} icon={icon} />;
        })}
      </ul>
    </div>
  );
};

export default NavigationBlock;
