import { useEffect, useMemo, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import BasicInfo from './BasicInfo';
import ProfileSetup from './ProfileSetup';
import Skills from './Skills';
import WorkExperience from './WorkExperience';
import Packages from './Packages';
import AdditionalInfo from './AdditionalInfo';
import {
  TRootState,
  updateAdditionalInfo,
  updateBasicInfo,
  updatePackages,
  updateProfileSetup,
  updateSkills,
  updateWorkExp,
  useFetchProfileQuery,
  useUpdateProfileMutation,
} from '../../../state/store';
import { useNavigate } from 'react-router-dom';

const EditProfileForm = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [updateProfile] = useUpdateProfileMutation();
  const user = useSelector((store: TRootState) => store.user);
  const additionalInfo = useSelector((store: TRootState) => store.additionalInfo);
  const profileSetup = useSelector((store: TRootState) => store.profileSetup);
  const basicInfo = useSelector((store: TRootState) => store.basicInfo);
  const skills = useSelector((store: TRootState) => store.skills);
  const workExp = useSelector((store: TRootState) => store.workExp);
  const pckg = useSelector((store: TRootState) => store.package);
  const [formStep, setFormStep] = useState(0);

  const canShowNextButton = useMemo(() => {
    return (
      (formStep < 5 && user.user.role.toLowerCase() === 'reviewer') ||
      (formStep < 1 && user.user.role.toLowerCase() === 'user')
    );
  }, [formStep, user.user.role]);

  const queryParameters = useMemo(
    () => ({ token: user.token, profileId: user.user.profileId }),
    [user.token, user.user.profileId]
  );

  const { data } = useFetchProfileQuery(queryParameters);

  useEffect(() => {
    if (data) {
      dispatch(updateBasicInfo(data.data.basicInfo));
      dispatch(updateProfileSetup(data.data.profileSetup));
      dispatch(updateSkills(data.data.skills));
      dispatch(updateWorkExp(data.data.workExp));
      dispatch(updatePackages(data.data.pckg));
      dispatch(updateAdditionalInfo(data.data.additionalInfo));
    }
  }, [data]);

  const renderFormStep = () => {
    if (user.user.role.toLowerCase() === 'user') {
      switch (formStep) {
        case 0:
          return <BasicInfo />;
        case 1:
          return <ProfileSetup />;
        default:
          return <div>You do not have permission to view this content.</div>;
      }
    }

    if (user.user.role.toLowerCase() === 'reviewer') {
      switch (formStep) {
        case 0:
          return <BasicInfo />;
        case 1:
          return <ProfileSetup />;
        case 2:
          return <Skills />;
        case 3:
          return <WorkExperience />;
        case 4:
          return <Packages />;
        case 5:
          return <AdditionalInfo />;
        default:
          return <BasicInfo />;
      }
    }

    return <div>You do not have permission to view this content.</div>;
  };

  const packageFormData = () => {
    const formData = {} as any;

    formData.contactNumber = basicInfo.contactNumber.value;
    formData.email = basicInfo.email.value;
    formData.fullName = basicInfo.fullName.value;
    formData.userName = basicInfo.userName.value;
    formData.bio = profileSetup.bio.value;
    formData.tagLine = profileSetup.tagLine.value;
    formData.languages = skills.languages;
    formData.programmingLanguages = skills.programmingLanguages;
    formData.qualifications = skills.qualifications;
    formData.workExps = workExp.workExps;
    formData.basic = pckg.basic;
    formData.standard = pckg.standard;
    formData.pro = pckg.pro;
    formData.availability = additionalInfo.availability;
    formData.moreInfo = additionalInfo.moreInfo;
    return formData;
  };

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const formData = packageFormData();

    updateProfile({ profileId: user.user.profileId, token: user.token, formData })
      .unwrap()
      .then(() => {
        navigate(`/settings/${user.user.slug}/profile`);
      });
  };

  return (
    <section className="border rounded-lg p-4 max-w-[650px] border-slate-800 my-8">
      <form onSubmit={handleOnSubmit}>
        {renderFormStep()}
        <div className="my-8 justify-center flex">
          {formStep > 0 && (
            <button
              onClick={() => setFormStep((prevState) => prevState - 1)}
              type="button"
              className="outline-btn bg-gray-400 min-w-24 mx-4"
            >
              Back
            </button>
          )}
          {canShowNextButton && (
            <button
              onClick={() => setFormStep((prevState) => prevState + 1)}
              type="button"
              className="outline-btn bg-gray-400 min-w-24 mx-4"
            >
              Next
            </button>
          )}
          {(formStep === 5 || (formStep === 1 && user.user.role.toLowerCase() === 'user')) && (
            <button type="submit" className="btn mx-4 min-w-24">
              Update
            </button>
          )}
        </div>
      </form>
    </section>
  );
};

export default EditProfileForm;
