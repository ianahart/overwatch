export interface ITopicDetailsHeaderProps {
  title: string;
}

const TopicDetailsHeader = ({ title }: ITopicDetailsHeaderProps) => {
  return (
    <header className="flex">
      <h2 className="text-3xl text-gray-400">{title}</h2>
    </header>
  );
};

export default TopicDetailsHeader;
