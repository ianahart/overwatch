import { IFullProfile } from '../../interfaces';
import BasicInfo from './BasicInfo';

export interface IUserProfileProps {
  profile: IFullProfile;
}

const UserProfile = ({ profile }: IUserProfileProps) => {
  return (
    <BasicInfo
      userId={profile.userProfile.userId}
      city={profile.userProfile.city}
      avatarUrl={profile.profileSetup.avatar}
      fullName={profile.basicInfo.fullName}
      country={profile.userProfile.country}
      abbreviation={profile.userProfile.abbreviation}
    />
  );
};

export default UserProfile;
