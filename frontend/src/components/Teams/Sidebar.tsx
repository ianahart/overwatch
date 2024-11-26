import AdminTeams from './AdminTeams';
import CreateTeamBtn from './CreateTeamBtn';

const Sidebar = () => {
  return (
    <div className="bg-stone-950 p-1 max-w-[275px] w-full flex-grow-[1] text-gray-400">
      <h2 className="text-xl text-center">Your Teams</h2>
      <CreateTeamBtn />
      <div className="my-4">
        <AdminTeams />
      </div>
    </div>
  );
};

export default Sidebar;
