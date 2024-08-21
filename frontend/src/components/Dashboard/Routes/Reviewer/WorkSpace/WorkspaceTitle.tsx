import { BsTrash } from 'react-icons/bs';
import { useSelector, useDispatch } from 'react-redux';
import { TRootState, updateWorkSpaceProperty, useCreateWorkSpaceMutation } from '../../../../../state/store';
import { useState } from 'react';
import WorkSpaceBackgroundPicker from './WorkSpaceBackgroundPicker';

const WorkSpaceTitle = () => {
  const dispatch = useDispatch();
  const [createWorkSpace] = useCreateWorkSpaceMutation();
  const { token, user } = useSelector((store: TRootState) => store.user);
  const { workSpace } = useSelector((store: TRootState) => store.workSpace);
  const [error, setError] = useState('');
  const [isActive, setIsActive] = useState(false);
  const [inputValue, setInputValue] = useState(() => (workSpace.title ? workSpace.title : ''));

  const applyServerErrors = <T extends object>(data: T) => {
    for (const [_, val] of Object.entries(data)) {
      setError(val);
    }
  };

  const handleOnBlur = () => {
    setError('');
    const payload = { token, userId: user.id, workSpace: { title: inputValue, backgroundColor: '' } };
    createWorkSpace(payload)
      .unwrap()
      .then((res) => {
        dispatch(updateWorkSpaceProperty({ value: res.data.title, property: 'title' }));
        setIsActive(false);
      })
      .catch((err) => {
        applyServerErrors(err.data);
      });
  };

  return (
    <>
      {error.length > 0 && <div className="flex my-1 text-red-400 text-sm justify-start">{error}</div>}
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
        {workSpace.title.length > 0 && (
          <div className="flex items-center">
            <div className="mx-2">
              <BsTrash className="text-gray-400" />
            </div>
            <div className="mx-2">
              <WorkSpaceBackgroundPicker />
            </div>
          </div>
        )}
      </div>
    </>
  );
};

export default WorkSpaceTitle;
