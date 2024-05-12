import { Outlet } from 'react-router-dom';
import Navigation from './Navigation';

const Settings = () => {
  return (
    <div className="min-h-[90vh]">
      <div className="max-w-[1280px] border border-slate-800 rounded mt-4 mx-auto min-h-[90vh] md:flex">
        <Navigation />
        <div className="min-h-[90vh] mt-4 w-full p-4 border">
          <Outlet />
        </div>
      </div>
    </div>
  );
};

export default Settings;
