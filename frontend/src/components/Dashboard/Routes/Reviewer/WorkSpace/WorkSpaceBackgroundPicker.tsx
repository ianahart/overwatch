import { useState } from 'react';
import { BsThreeDots } from 'react-icons/bs';
import ClickAway from '../../../../Shared/ClickAway';
import { backgroundColors } from '../../../../../data';
import { useDispatch } from 'react-redux';
import { updateWorkSpaceProperty } from '../../../../../state/store';

const WorkSpaceBackgroundPicker = () => {
  const dispatch = useDispatch();
  const [isOpen, setIsOpen] = useState(false);

  const handleClickAway = () => {
    setIsOpen(false);
  };

  const handleSelectBackgroundColor = (backgroundColor: string) => {
    dispatch(updateWorkSpaceProperty({ value: backgroundColor, property: 'backgroundColor' }));
    setIsOpen(false);
  };

  return (
    <div className="relative">
      <BsThreeDots onClick={() => setIsOpen(!isOpen)} className="text-gray-400 cursor-pointer" />
      {isOpen && (
        <ClickAway onClickAway={handleClickAway}>
          <div className="absolute top-6 right-0 bg-slate-800 p-1 rounded md:w-52 w-40 shadow-md">
            <p className="text-sm text-gray-400">Workspace background color</p>
            <div className="flex flex-wrap">
              {backgroundColors.map(({ id, value }) => {
                return (
                  <div
                    onClick={() => handleSelectBackgroundColor(value)}
                    style={{ backgroundColor: value }}
                    key={id}
                    className="w-6 h-6 rounded m-1 p-1 cursor-pointer hover:opacity-80"
                  ></div>
                );
              })}
            </div>
          </div>
        </ClickAway>
      )}
    </div>
  );
};

export default WorkSpaceBackgroundPicker;
