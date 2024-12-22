//import { useFetchProfileQuery } from '../../state/store';
//import UserProfile from './UserProfile';
//import ReviewerProfile from './ReviewerProfile';
//import { useEffect, useState } from 'react';
//import { profileState } from '../../data';
//import { IFullProfile } from '../../interfaces';
//import { retrieveTokens } from '../../util';
//
export interface IProfileProps {
  profileId: number;
}

const Profile = ({ profileId }: IProfileProps) => {
  console.log(profileId);
  //  const tokens = retrieveTokens();
  //  const { data } = useFetchProfileQuery({ profileId, token: tokens.accessToken });
  //  const [profile, setProfile] = useState<IFullProfile>(profileState);
  //
  //  useEffect(() => {
  //    if (data) {
  //      setProfile(data.data);
  //    }
  //  }, [data]);
  //
  return (
    <div>
      {/*     {profile.userProfile.role === 'REVIEWER' ? (
        <ReviewerProfile profile={profile} />
      ) : (
        <UserProfile profile={profile} />
      )}*/}
    </div>
  );
};

export default Profile;
