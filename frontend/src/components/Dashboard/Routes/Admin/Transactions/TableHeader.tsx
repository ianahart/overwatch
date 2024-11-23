export interface ITableHeaderProps {
  heading: string;
}

const TableHeader = ({ heading }: ITableHeaderProps) => {
  return <th className="border px-4 py-2 text-left bg-blue-400 text-black text-sm md:text-base">{heading}</th>;
};

export default TableHeader;
