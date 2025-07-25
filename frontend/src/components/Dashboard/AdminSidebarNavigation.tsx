import { FaComment, FaMoneyBill, FaNewspaper, FaPlusCircle, FaRibbon, FaShieldAlt } from 'react-icons/fa';
import { GrTransaction } from 'react-icons/gr';

import DashboardTitle from './DashboardTitle';
import { AiOutlineComment, AiOutlineUser } from 'react-icons/ai';
import NavigationBlock from './Routes/Admin/NavigationBlock';
import { TbUsersGroup } from 'react-icons/tb';

const AdminSidebarNavigation = () => {
  const transactionLinks = [
    { path: 'refunds', label: 'Refunds', id: 2, icon: <FaMoneyBill /> },
    { path: 'transactions', label: 'Transactions', id: 3, icon: <GrTransaction /> },
  ];
  const userLinks = [
    { path: 'banned-users', label: 'Banned Users', id: 4, icon: <AiOutlineUser /> },
    { path: 'all-users', label: 'All Users', id: 10, icon: <TbUsersGroup /> },
  ];
  const contentLinks = [
    { path: 'flagged-comments', label: 'Flagged Comments', id: 1, icon: <FaComment /> },
    { path: 'testimonials', label: 'Testimonials', id: 6, icon: <FaNewspaper /> },
    { path: 'suggestions', label: 'Suggestions', id: 7, icon: <AiOutlineComment /> },
  ];

  const badgeLinks = [
    { path: 'badges/create', label: 'Create a Badge', id: 8, icon: <FaPlusCircle /> },
    { path: 'badges', label: 'Badges', id: 9, icon: <FaRibbon /> },
  ];

  const securityLinks = [
    { path: 'multifactor-authentication', label: 'MF Authentication', id: 5, icon: <FaShieldAlt /> },
  ];

  return (
    <div data-testid="AdminSidebarNavigation" className="md:w-[300px] min-h-[90vh]">
      <div className="p-4">
        <DashboardTitle title="OverWatch" version={0.1} />
        <p className="text-gray-400 mt-2 text-sm font-bold">Admin</p>
      </div>
      <div className="my-8">
        <NavigationBlock links={transactionLinks} title="Transaction" />
        <NavigationBlock links={userLinks} title="User" />
        <NavigationBlock links={contentLinks} title="Content" />
        <NavigationBlock links={badgeLinks} title="Badges" />
        <NavigationBlock links={securityLinks} title="Security" />
      </div>
    </div>
  );
};

export default AdminSidebarNavigation;
