export interface IHeaderProps {
  heading: string;
}

const Header = ({ heading }: IHeaderProps) => {
  return <h3 className="text-2xl text-gray-400">{heading}</h3>;
};

export default Header;
