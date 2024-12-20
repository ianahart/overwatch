import { useSelector } from 'react-redux';
import { GoCodeReview } from 'react-icons/go';
import { GiCheckboxTree } from 'react-icons/gi';
import { VscGraphLine } from 'react-icons/vsc';

import DashboardTitle from './DashboardTitle';
import { TRootState } from '../../state/store';
import DashboardNavigationLink from './DashboardNavigationLink';
import { BsKanban } from 'react-icons/bs';
import { FaPlus, FaRibbon } from 'react-icons/fa';

const ReviewerSidebarNavigation = () => {
  const { user } = useSelector((store: TRootState) => store.user);
  const links = [
    { path: 'reviewer/reviews', label: 'Your Reviews', id: 2, icon: <GoCodeReview /> },
    { path: `/settings/${user.slug}/connects`, label: 'Your Connects', id: 3, icon: <GiCheckboxTree /> },
    { path: 'reviewer/stats', label: 'Your Statistics', id: 4, icon: <VscGraphLine /> },
    { path: 'reviewer/workspaces', label: 'Your Workspaces', id: 5, icon: <BsKanban /> },
    { path: 'reviewer/suggestions/create', label: 'Add Suggestions', id: 6, icon: <FaPlus /> },
    { path: 'reviewer/badges', label: 'Badges', id: 7, icon: <FaRibbon /> },
  ];

  return (
    <div className="md:w-[300px] min-h-[90vh]">
      <div className="p-4">
        <DashboardTitle title="OverWatch" version={0.1} />
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

export default ReviewerSidebarNavigation;
