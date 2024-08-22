import { useSelector } from 'react-redux';
import { GoCodeReview } from 'react-icons/go';
import { GiCheckboxTree } from 'react-icons/gi';
import { VscGraphLine } from 'react-icons/vsc';

import DashboardTitle from './DashboardTitle';
import { TRootState } from '../../state/store';
import DashboardNavigationLink from './DashboardNavigationLink';
import { BsKanban } from 'react-icons/bs';

const ReviewerSidebarNavigation = () => {
  const { user } = useSelector((store: TRootState) => store.user);
  const links = [
    { path: 'reviewer/reviews', label: 'Your Reviews', id: 2, icon: <GoCodeReview /> },
    { path: `/settings/${user.slug}/connects`, label: 'Connects', id: 3, icon: <GiCheckboxTree /> },
    { path: 'reviewer/stats', label: 'Your statistics', id: 4, icon: <VscGraphLine /> },
    { path: 'reviewer/workspaces', label: 'Your Workspaces', id: 5, icon: <BsKanban /> },
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
