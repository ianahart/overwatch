import { Outlet } from 'react-router-dom';
import { useEffect } from 'react';
import { useDispatch } from 'react-redux';

import Sidebar from './Sidebar';
import Navigation from './Navigation';
import { clearTeams } from '../../state/store';

const Teams = () => {
  const dispatch = useDispatch();

  useEffect(() => {
    return () => {
      dispatch(clearTeams());
    };
  }, [dispatch]);

  return (
    <div className="flex xl:flex-row flex-col min-h-[90vh]">
      <Sidebar />
      <div className="border-gray-800 rounded p-2 mx-2 flex-grow-[2]">
        <div className="my-8">
          <Navigation />
        </div>
        <Outlet />
      </div>
    </div>
  );
};

export default Teams;
