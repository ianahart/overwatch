import { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';

import Switch from '../Switch';
import { TRootState, useFetchProfileVisibilityQuery, useUpdateProfileVisibilityMutation } from '../../../state/store';

const ProfileVisibility = () => {
  const { user, token } = useSelector((store: TRootState) => store.user);
  const [updateProfileVisibilityMut] = useUpdateProfileVisibilityMutation();
  const { data, refetch } = useFetchProfileVisibilityQuery(
    { profileId: user.profileId, token },
    { skip: !user.profileId || !token }
  );
  const [isToggled, setIsToggled] = useState(true);

  useEffect(() => {
    if (data !== undefined) {
      setIsToggled(data.data);
    }
  }, [data, refetch]);

  const handleSwitchToggled = (switchToggled: boolean): void => {
    updateProfileVisibilityMut({ profileId: user.profileId, token, isVisible: switchToggled })
      .unwrap()
      .then((res) => {
        setIsToggled(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div className="my-8">
      <h3 className="text-xl mb-4">Profile Visiblity</h3>
      <p className="text-sm">Want to take a break?</p>
      <p className="text-sm">Toggling this on and off will show and hide your profile from the explore page.</p>
      <Switch handleSwitchToggled={handleSwitchToggled} switchToggled={isToggled} />
    </div>
  );
};

export default ProfileVisibility;
