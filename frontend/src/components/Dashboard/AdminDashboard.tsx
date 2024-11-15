import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { useEffect } from 'react';

import { TRootState } from '../../state/store';
import AdminSidebarNaivigation from './AdminSidebarNavigation';
import OutletContainer from './OutletContainer';

const AdminDashboard = () => {
  const { user } = useSelector((store: TRootState) => store.user);
  const navigate = useNavigate();

  useEffect(() => {
    if (user.role === 'ADMIN') {
      navigate(`/admin/dashboard/${user.slug}`);
    } else {
      navigate('/');
    }
  }, [user.role, user.slug, navigate]);

  return (
    <div className="md:max-w-[1450px] w-full mx-auto mt-8">
      <div className="md:flex bg-gray-900 p-2 rounded">
        <AdminSidebarNaivigation />
        <OutletContainer />
      </div>
    </div>
  );
};

export default AdminDashboard;
