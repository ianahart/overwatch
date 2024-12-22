import { useSelector } from 'react-redux';
import { TRootState } from '../../state/store';
import UserProfile from './UserProfile';
import ReviewerProfile from './ReviewerProfile';
import {  useState } from 'react';
import { profileState } from '../../data';
import { IFullProfile } from '../../interfaces';

export interface IProfileProps {
  profileId: number;
}

const Profile = ({ profileId }: IProfileProps) => {
  const { token } = useSelector((store: TRootState) => store.user);
  //  const { data } = useFetchProfileQuery({ profileId, token });
  const [profile, setProfile] = useState<IFullProfile>(profileState);
  console.log(token, profileId, setProfile);

  //  useEffect(() => {
  //    if (data && token) {
  //      setProfile(data.data);
  //    }
  //  }, [data, token]);
  //
  return (
    <div>
      {profile.userProfile.role === 'REVIEWER' ? (
        <ReviewerProfile profile={profile} />
      ) : (
        <UserProfile profile={profile} />
      )}
    </div>
  );
};

export default Profile;
