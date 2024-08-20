import { Outlet } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import WorkSpaceTitle from './WorkspaceTitle';

const WorkSpaceContainer = () => {
  const navigate = useNavigate();

  const handleOnClick = () => {
    navigate(`/dashboard/danweatherman/reviewer/workspaces/1`);
  };
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
