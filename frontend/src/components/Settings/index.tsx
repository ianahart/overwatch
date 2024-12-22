import { Outlet } from 'react-router-dom';
import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import Navigation from './Navigation';
import { TRootState, updateSetting, useFetchSettingsQuery } from '../../state/store';

const Settings = () => {
  const dispatch = useDispatch();
  const { token, user } = useSelector((store: TRootState) => store.user);

  const { data, isSuccess } = useFetchSettingsQuery(
    {
      token,
      settingId: user.settingId,
    },
    { skip: !token || !user.settingId }
  );

  useEffect(() => {
    if (isSuccess) {
      const { data: res } = data;
      const updatedSetting = { ...res, createdAt: res.createdAt.toLocaleString() };
      dispatch(updateSetting(updatedSetting));
    }
  }, [isSuccess, data, dispatch]);
  return (
    <div className="min-h-[90vh]">
      <div className="max-w-[1480px] w-full border border-slate-800 rounded mt-4 mx-auto min-h-[90vh] md:flex">
        <Navigation />
        <div className="min-h-[90vh] mt-4 w-full p-4">
          <Outlet />
        </div>
      </div>
    </div>
  );
};

export default Settings;
