import { FaComment, FaMoneyBill } from 'react-icons/fa';
import { GrTransaction } from 'react-icons/gr';

import DashboardTitle from './DashboardTitle';
import DashboardNavigationLink from './DashboardNavigationLink';

const AdminSidebarNaivigation = () => {
  const links = [
    { path: 'flagged-comments', label: 'Flagged Comments', id: 1, icon: <FaComment /> },
    { path: 'refunds', label: 'Refunds', id: 2, icon: <FaMoneyBill /> },
    { path: 'transactions', label: 'Transactions', id: 3, icon: <GrTransaction /> },
  ];

  return (
    <div className="md:w-[300px] min-h-[90vh]">
      <div className="p-4">
        <DashboardTitle title="OverWatch" version={0.1} />
        <p className="text-gray-400 mt-2 text-sm font-bold">Admin</p>
      </div>
      <div className="my-8">
        <ul>
          {links.map(({ path, label, id, icon }) => {
            return <DashboardNavigationLink key={id} path={path} label={label} icon={icon} />;
          })}
        </ul>
      </div>
    </div>
  );
};

export default AdminSidebarNaivigation;
