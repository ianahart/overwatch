import { Outlet } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import WorkSpaceTitle from './WorkspaceTitle';
import { useEffect } from 'react';
import { useDispatch } from 'react-redux';
import { clearWorkSpace } from '../../../../../state/store';

const WorkSpaceContainer = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleOnClick = () => {
    navigate(`/dashboard/danweatherman/reviewer/workspaces/1`);
  };

  useEffect(() => {
    return () => {
      dispatch(clearWorkSpace());
    };
  }, []);

  return (
    <div>
      <WorkSpaceTitle />
      <button className="btn" onClick={handleOnClick}>
        go to outlet route
      </button>
      <Outlet />
    </div>
  );
};

export default WorkSpaceContainer;
