export interface ICommunityHeaderProps {
  heading: string;
  description?: string;
}

const CommunityHeader = ({ heading, description }: ICommunityHeaderProps) => {
  return (
    <div>
      <header className="flex flex-col items-center text-gray-400 my-8">
        <h2 className="text-3xl">{heading}</h2>
        <p>{description}</p>
      </header>
    </div>
  );
};

export default CommunityHeader;
