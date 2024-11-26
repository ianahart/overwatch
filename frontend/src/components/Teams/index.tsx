import { Outlet } from 'react-router-dom';
import Sidebar from './Sidebar';
import Navigation from './Navigation';

const Teams = () => {
  return (
    <div className="flex xl:flex-row flex-col min-h-[90vh]">
      <Sidebar />
      <div className="border border-gray-800 rounded p-2 mx-2 flex-grow-[2]">
        <div className="my-8">
          <Navigation />
        </div>
        <Outlet />
      </div>
    </div>
  );
};

export default Teams;
