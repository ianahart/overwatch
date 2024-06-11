import { IFullProfile } from '../../interfaces';
import AdditionalInfo from './AdditionalInfo';
import BasicInfo from './BasicInfo';
import ContactInfo from './ContactInfo';
import Pckgs from './Pckgs';
import Skills from './Skills';
import Testimonials from './Testimonials';
import TimeSlots from './TimeSlots';
import WorkExp from './WorkExp';

export interface IReviewerProfileProps {
  profile: IFullProfile;
}

const ReviewerProfile = ({ profile }: IReviewerProfileProps) => {
  return (
    <>
      <BasicInfo
        userId={profile.userProfile.userId}
        city={profile.userProfile.city}
        avatarUrl={profile.profileSetup.avatar}
        fullName={profile.basicInfo.fullName}
        country={profile.userProfile.country}
        abbreviation={profile.userProfile.abbreviation}
      />
      <div className="md:flex">
        <div className="sidebar">
          <ContactInfo
            email={profile.basicInfo.email}
            contactNumber={profile.basicInfo.contactNumber}
            userName={profile.basicInfo.userName}
          />
          <Skills
            languages={profile.skills.languages ?? []}
            programmingLanguages={profile.skills.programmingLanguages ?? []}
            qualifications={profile.skills.qualifications ?? []}
          />
          <TimeSlots availability={profile.additionalInfo.availability ?? []} />
        </div>
        <div className="border border-t-0 border-b-0 border-gray-800 w-full">
          <WorkExp workExps={profile.workExp.workExps ?? []} />
          {profile.pckg.basic !== null && profile.pckg.standard !== null && profile.pckg.pro !== null && (
            <Pckgs basic={profile.pckg.basic} standard={profile.pckg.standard} pro={profile.pckg.pro} />
          )}
          <AdditionalInfo
            moreInfo={profile.additionalInfo.moreInfo}
            bio={profile.profileSetup.bio}
            tagLine={profile.profileSetup.tagLine}
          />
          <Testimonials userId={profile.userProfile.userId} />
        </div>
      </div>
    </>
  );
};

export default ReviewerProfile;
