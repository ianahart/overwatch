import { useDispatch, useSelector } from 'react-redux';
import { useEffect } from 'react';

import MultiFactorAuthentication from '../../../../Settings/Security/MultiFactorAuthentication';
import { TRootState, updateSetting, useFetchSettingsQuery } from '../../../../../state/store';

const MFAuthentication = () => {
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

  return <MultiFactorAuthentication />;
};

export default MFAuthentication;
