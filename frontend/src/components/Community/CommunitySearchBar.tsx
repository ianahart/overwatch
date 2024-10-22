export interface ICommunitySearchBarProps {
  btnText: string;
}

const CommunitySearchBar = ({ btnText }: ICommunitySearchBarProps) => {
  return (
    <div className="flex items-center justify-center w-full">
      <input
        placeholder="Search a topic..."
        className="border border-gray-800 bg-transparent h-9 rounded p-2 md:w-[60%] w-[100%]"
      />
      <button className="btn md:ml-2">{btnText}</button>
    </div>
  );
};

export default CommunitySearchBar;
