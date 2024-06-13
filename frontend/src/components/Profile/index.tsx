import { useSelector } from 'react-redux';
import { TRootState, useFetchProfileQuery } from '../../state/store';
import UserProfile from './UserProfile';
import ReviewerProfile from './ReviewerProfile';
import { useEffect, useState } from 'react';
import { profileState } from '../../data';
import { IFullProfile } from '../../interfaces';

export interface IProfileProps {
  profileId: number;
}

const Profile = ({ profileId }: IProfileProps) => {
  console.log(profileId);
  const { token } = useSelector((store: TRootState) => store.user);
  const { data } = useFetchProfileQuery({ profileId, token });
  const [profile, setProfile] = useState<IFullProfile>(profileState);

  useEffect(() => {
    if (data) {
      console.log(data);
      setProfile(data.data);
    }
  }, [data]);

  return (
    <>
      {profile.userProfile.role === 'REVIEWER' ? (
        <ReviewerProfile profile={profile} />
      ) : (
        <UserProfile profile={profile} />
      )}
    </>
  );
};

export default Profile;
