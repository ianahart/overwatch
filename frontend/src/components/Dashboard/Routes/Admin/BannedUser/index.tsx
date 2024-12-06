import { useState } from 'react';
import BannedUserList from './BannedUserList';
import BannedUserForm from './BannedUserForm';

const BannedUser = () => {
  const [view, setView] = useState('list');

  const handleSetView = (view: string): void => {
    setView(view);
  };
  return (
    <div className="md:max-w-[1450px] w-full mx-auto mt-8">
      <div className="bg-gray-900 p-2 rounded">
        <div className="flex justify-end text-gray-400 font-bold">
          <button onClick={() => setView('list')} className="mx-2 hover:text-gray-600">
            Banned Users
          </button>
          <button onClick={() => setView('form')} className="mx-2 hover:text-gray-600">
            Create Ban
          </button>
        </div>
        <div className="mt-10 mb-4">
          {view === 'list' && <BannedUserList handleSetView={handleSetView} />}
          {view === 'form' && <BannedUserForm formType="create" handleSetView={handleSetView} />}
        </div>
      </div>
    </div>
  );
};
export default BannedUser;
