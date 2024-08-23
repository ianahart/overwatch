import { BsTrash, BsPlus } from 'react-icons/bs';
import { useSelector, useDispatch } from 'react-redux';
import {
  TRootState,
  setWorkSpace,
  updateWorkSpaceProperty,
  useCreateWorkSpaceMutation,
  useDeleteWorkSpaceMutation,
  useEditWorkSpaceMutation,
} from '../../../../../state/store';
import { useState } from 'react';
import WorkSpaceBackgroundPicker from './WorkSpaceBackgroundPicker';
import { ICreateWorkSpaceRequest, IUpdateWorkSpaceRequest } from '../../../../../interfaces';

const WorkSpaceTitle = () => {
  const dispatch = useDispatch();
  const [deleteWorkSpace] = useDeleteWorkSpaceMutation();
  const [createWorkSpace] = useCreateWorkSpaceMutation();
  const [updateWorkSpace] = useEditWorkSpaceMutation();
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

  const handleCreateWorkSpace = (payload: ICreateWorkSpaceRequest) => {
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

  const handleUpdateWorkSpace = (payload: IUpdateWorkSpaceRequest) => {
    updateWorkSpace(payload)
      .unwrap()
      .then((res) => {
        dispatch(setWorkSpace(res.data));
        setIsActive(false);
      })
      .catch((err) => {
        applyServerErrors(err.data);
      });
  };

  const handleOnBlur = () => {
    if (inputValue.trim().length === 0) return;
    setError('');
    const payload = { token, userId: user.id, workSpace: { title: inputValue, backgroundColor: '' } };

    if (workSpace.id === 0) {
      handleCreateWorkSpace(payload);
    } else {
      handleUpdateWorkSpace({ ...payload, id: workSpace.id });
    }
  };

  const emptyWorkSpace = () => {
    dispatch(setWorkSpace({ userId: 0, id: 0, createdAt: '', title: '', backgroundColor: '' }));
  };

  const handleDeleteWorkSpace = () => {
    deleteWorkSpace({ token, id: workSpace.id })
      .unwrap()
      .then(() => {
        emptyWorkSpace();
      })
      .catch((err) => {
        console.log(err);
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
            <div className="mx-2 cursor-pointer" onClick={emptyWorkSpace}>
              <BsPlus className="text-xl" />
            </div>
            <div className="mx-2 cursor-pointer" onClick={handleDeleteWorkSpace}>
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
