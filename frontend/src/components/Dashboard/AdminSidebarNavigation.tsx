import { FaComment, FaMoneyBill, FaNewspaper, FaShieldAlt } from 'react-icons/fa';
import { GrTransaction } from 'react-icons/gr';

import DashboardTitle from './DashboardTitle';
import { AiOutlineUser } from 'react-icons/ai';
import NavigationBlock from './Routes/Admin/NavigationBlock';

const AdminSidebarNavigation = () => {
  const transactionLinks = [
    { path: 'refunds', label: 'Refunds', id: 2, icon: <FaMoneyBill /> },
    { path: 'transactions', label: 'Transactions', id: 3, icon: <GrTransaction /> },
  ];
  const userLinks = [{ path: 'banned-users', label: 'Banned Users', id: 4, icon: <AiOutlineUser /> }];
  const contentLinks = [
    { path: 'flagged-comments', label: 'Flagged Comments', id: 1, icon: <FaComment /> },
    { path: 'testimonials', label: 'Testimonials', id: 6, icon: <FaNewspaper /> },
  ];
  const securityLinks = [
    { path: 'multifactor-authentication', label: 'MF Authentication', id: 5, icon: <FaShieldAlt /> },
  ];

  return (
    <div className="md:w-[300px] min-h-[90vh]">
      <div className="p-4">
        <DashboardTitle title="OverWatch" version={0.1} />
        <p className="text-gray-400 mt-2 text-sm font-bold">Admin</p>
      </div>
      <div className="my-8">
        <NavigationBlock links={transactionLinks} title="Transaction" />
        <NavigationBlock links={userLinks} title="User" />
        <NavigationBlock links={contentLinks} title="Content" />
        <NavigationBlock links={securityLinks} title="Security" />
      </div>
    </div>
  );
};

export default AdminSidebarNavigation;
