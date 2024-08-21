import {  BsTrash } from 'react-icons/bs';
import { useSelector, useDispatch } from 'react-redux';
import { TRootState, updateWorkSpaceProperty } from '../../../../../state/store';
import { useState } from 'react';
import WorkSpaceBackgroundPicker from './WorkSpaceBackgroundPicker';

const WorkSpaceTitle = () => {
  const dispatch = useDispatch();
  const { workSpace } = useSelector((store: TRootState) => store.workSpace);
  const [isActive, setIsActive] = useState(false);
  const [inputValue, setInputValue] = useState(() => (workSpace.title ? workSpace.title : ''));

  const handleOnBlur = () => {
    console.log('saving title');
    dispatch(updateWorkSpaceProperty({ value: inputValue, property: 'title' }));
    setIsActive(false);
  };

  return (
    <div className="flex justify-between bg-slate-900 py-4 px-2 rounded mb-2">
      {!isActive && (
        <p onClick={() => setIsActive(true)} className="cursor-pointer">
          {!workSpace.title ? 'Add workspace title' : workSpace.title}
        </p>
      )}
      {isActive && (
        <input
          onChange={(e) => setInputValue(e.target.value)}
          className="bg-transparent border rounded border-gray-800 h-9"
          onBlur={handleOnBlur}
        />
      )}
      <div className="flex items-center">
        <div className="mx-2">
          <BsTrash className="text-gray-400" />
        </div>
        <div className="mx-2">
          <WorkSpaceBackgroundPicker />
        </div>
      </div>
    </div>
  );
};

export default WorkSpaceTitle;
