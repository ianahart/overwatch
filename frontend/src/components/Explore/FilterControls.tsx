export interface IFilterControlsProps {
  handleSetFilter: (value: string, desc: string) => void;
  fetchProfiles: (paginate: boolean, filterValue: string) => Promise<void>;
  filter: { value: string; desc: string };
}
const FilterControls = ({ handleSetFilter, fetchProfiles, filter }: IFilterControlsProps) => {
  const filters = [
    {
      id: 1,
      name: 'Most Recent',
      value: 'most-recent',
      desc: 'Browse Reviewers that have just signed up and our new to the platform.',
    },
    { id: 2, name: 'US Only', value: 'domestic', desc: 'Find Reviewers who are local to the US who speak English.' },
    { id: 3, name: 'Saved', value: 'saved', desc: 'Filter through Reviewers who you have saved to your favorites.' },
    {
      id: 4,
      name: 'Most Relevant',
      value: 'most-relevant',
      desc: 'Sort through the Reviewers that match your programming languages.',
    },
  ];

  const handleOnClick = async (value: string, desc: string) => {
    handleSetFilter(value, desc);
    await fetchProfiles(false, value);
  };

  return (
    <ul className="flex items-center border-b border-gray-800 w-full">
      {filters.map(({ id, name, value, desc }) => {
        return (
          <li
            className={`px-2 cursor-pointer ${filter.value === value ? 'active-link' : ''}`}
            key={id}
            onClick={() => handleOnClick(value, desc)}
          >
            {name}
          </li>
        );
      })}
    </ul>
  );
};

export default FilterControls;
