import { useEffect, useRef, useState } from 'react';
import { BsChevronDown } from 'react-icons/bs';

export interface IFormSelectProps {
  data: { name: string; value: string; id: number }[];
  updateField: (name: string, value: string) => void;
  country: string;
}

const FormSelect = ({ data, country, updateField }: IFormSelectProps) => {
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const [countryName, setCountryName] = useState('United States');
  const [filteredCountries, setFilteredCountries] = useState(data);

  const ref = useRef<HTMLDivElement>(null);

  const handleClickOutside = (event: any) => {
    if (ref.current && !ref.current.contains(event.target)) {
      setIsDropdownOpen(false);
    }
  };

  const handleOnChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const country = e.target.value.toLowerCase();
    const updated = filteredCountries.filter(({ value }) => value.toLowerCase().includes(country));
    country.length === 0 ? setFilteredCountries(data) : setFilteredCountries(updated);
  };

  useEffect(() => {
    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  const handleOnClick = (name: string, value: string) => {
    setCountryName(name);
    updateField('country', value);
    setIsDropdownOpen(false);
  };

  return (
    <div className="max-w-[60%]">
      <p>Country</p>
      <div
        onClick={() => setIsDropdownOpen(true)}
        className="border rounded border-slate-800 flex items-center relative min-h-9 cursor-pointer"
      >
        <p className="m-0">{country}</p>
        <BsChevronDown className="absolute z-10 text-gray-400 right-2 top-2" />
        {isDropdownOpen && (
          <div ref={ref} className="absolute top-10 w-full bg-stone-950 left-0 z-10">
            <div className="m-1">
              <input
                onChange={handleOnChange}
                className="rounded bg-transparent border border-gray-800 w-full"
                placeholder="Search..."
              />
            </div>
            <ul className="w-full overflow-y-auto h-80">
              {filteredCountries.map(({ name, value, id }) => {
                return (
                  <li
                    className={`p-2 hover:bg-slate-950 ${countryName === name ? 'text-green-400' : 'text-inherit'}`}
                    key={id}
                    onClick={() => handleOnClick(name, value)}
                  >
                    {name}
                  </li>
                );
              })}
            </ul>
          </div>
        )}
      </div>
    </div>
  );
};

export default FormSelect;
