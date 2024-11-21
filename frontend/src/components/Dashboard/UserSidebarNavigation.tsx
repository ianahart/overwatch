import { useSelector } from 'react-redux';
import { GoCodeReview } from 'react-icons/go';
import { VscOpenPreview } from 'react-icons/vsc';
import { GiCheckboxTree } from 'react-icons/gi';
import { FaRegHandshake } from 'react-icons/fa';

import DashboardTitle from './DashboardTitle';
import { TRootState } from '../../state/store';
import DashboardNavigationLink from './DashboardNavigationLink';

const UserSidebarNavigation = () => {
  const { user } = useSelector((store: TRootState) => store.user);
  const links = [
    { path: 'user/add-review', label: 'Get a review', id: 1, icon: <VscOpenPreview /> },
    { path: 'user/reviews', label: 'Your Reviews', id: 2, icon: <GoCodeReview /> },
    { path: `/settings/${user.slug}/connects`, label: 'Your Connects', id: 3, icon: <GiCheckboxTree /> },
    { path: 'user/testimonials', label: 'Testimonials', id: 4, icon: <FaRegHandshake /> },
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

export default UserSidebarNavigation;
