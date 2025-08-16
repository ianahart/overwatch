import { BiChevronDown, BiChevronUp } from 'react-icons/bi';
import { useState } from 'react';

import CurrentWorkSpaceDropdown from './CurrentWorkSpaceDropdown';

const CurrentWorkSpaces = () => {
  const [isOpen, setIsOpen] = useState(false);

  const onClickAway = () => {
    setIsOpen(false);
  };

  const toggleDropdown = () => {
    setIsOpen((prevState) => !prevState);
  };

  const handleSetOpen = (open: boolean) => {
    setIsOpen(open);
  };
  return (
    <div className="relative min-h-full p-2 border rounded border-gray-800 my-2">
      <div data-testid="currentworkspace-trigger" onClick={toggleDropdown} className="flex items-center cursor-pointer">
        <h3>Your Workspaces</h3>
        <div className="text-2xl ml-5">
          {isOpen ? (
            <BiChevronDown data-testid="currentworkspace-chevron-down" />
          ) : (
            <BiChevronUp data-testid="currentworkspace-chevron-up" />
          )}
        </div>
      </div>
      {isOpen && <CurrentWorkSpaceDropdown onClickAway={onClickAway} handleSetOpen={handleSetOpen} />}
    </div>
  );
};
export default CurrentWorkSpaces;
