import { Outlet } from 'react-router-dom';
import WorkSpaceTitle from './WorkspaceTitle';
import { useEffect } from 'react';
import { useDispatch } from 'react-redux';
import { clearWorkSpace } from '../../../../../state/store';
import CurrentWorkSpaces from './CurrentWorkSpaces';

const WorkSpaceContainer = () => {
  const dispatch = useDispatch();

  useEffect(() => {
    return () => {
      dispatch(clearWorkSpace());
    };
  }, []);

  return (
    <div className="max-w-full overflow-hidden">
      <CurrentWorkSpaces />
      <WorkSpaceTitle />
      <div className="min-h-[700px]">
        <Outlet />
      </div>
    </div>
  );
};

export default WorkSpaceContainer;
